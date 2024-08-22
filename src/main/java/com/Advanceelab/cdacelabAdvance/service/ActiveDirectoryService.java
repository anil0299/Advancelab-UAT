package com.Advanceelab.cdacelabAdvance.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service
public class ActiveDirectoryService {

    private final String host = "10.10.87.202";
    private final String username = "Administrator";
    private final String password = "CDAC@123456";

    public String createUserInAD(String displayName, String password, String samAccountName, String userPrincipalName, String name) {
    	
        StringBuilder output = new StringBuilder();

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, 22);
            session.setPassword(this.password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String createUserCommand = String.format("powershell.exe New-ADUser -AccountPassword (ConvertTo-SecureString -AsPlainText '%s' -Force) " +
                                                      "-CannotChangePassword $true " +
                                                      "-PasswordNeverExpires $true " +
                                                      "-DisplayName '%s' " +
                                                      "-Enabled $true " +
                                                      "-Path 'OU=cybergyan,DC=cybergyan,DC=in' " +
                                                      "-SamAccountName '%s' " +
                                                      "-UserPrincipalName '%s' " +
                                                      "-PassThru " +
                                                      "-Name '%s';",
                                                      password, displayName, samAccountName, userPrincipalName, name);

            // Adding user to group
            String addUserToGroupCommand = String.format("powershell.exe Add-ADGroupMember -Identity 'CGUser' -Members '%s'", samAccountName);

            executeCommand(session, createUserCommand, output);
            executeCommand(session, addUserToGroupCommand, output);

            session.disconnect();

            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing commands: " + e.getMessage();
        }
    }
    
    public String removeUserInAD(String samAccountName) {
        StringBuilder output = new StringBuilder();

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Removing user from group
            String removeUserFromGroupCommand = String.format("powershell.exe Remove-ADGroupMember -Identity 'CGUser' -Members '%s' -Confirm:$false", samAccountName);
            // Removing user from AD
            String removeUserCommand = String.format("powershell.exe Remove-ADUser -Identity '%s' -Confirm:$false", samAccountName);

            executeCommand(session, removeUserFromGroupCommand, output);
            executeCommand(session, removeUserCommand, output);

            session.disconnect();

            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing commands: " + e.getMessage();
        }
    }

    private void executeCommand(Session session, String command, StringBuilder output) throws IOException, JSchException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);

        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        channelExec.disconnect();
    }
}