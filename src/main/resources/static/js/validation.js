
var origHPPCode = "e57f2d5b9fece53e23c2353e740aece6";
var origHPPCode2 = "axteopdgfgte";
 var seed =5;

function encodeData(input) {
	var output = "";
	for (var i = 0; i < input.length; i++) {
		var c = input.charCodeAt(i);
		if (c >= 65 && c <= 90) {
			output += String.fromCharCode((c - 65 + 3) % 26 + 65); // Uppercase
		}
		else if(c >= 48 && c<= 56)
			{
			
			output += String.fromCharCode((c - 48 + 3) % 26 + 48); //for Number(0-9)
			
			}
		else if (c >= 97 && c <= 122) {
			output += String.fromCharCode((c - 97 + 3) % 26 + 97); // Lowercase
		} else {
			output += String.fromCharCode(c);
		}
	}
	return output;
}




function getInputCode(input) {
    var output = "";

    for (var i = 0; i < input.length; i++) {

        var charCode = input.charCodeAt(i);
        output += String.fromCharCode(charCode);
    }
    return output;
}


function encode(input, key) {
	var output = "";

	for (var i = 0; i < input.length; i++) {
		var c = input.charCodeAt(i);

		if (c >= 65 && c <= 90) {
			output += String.fromCharCode((c - 65 + key) % 26 + 65); // Uppercase
		} else if (c >= 97 && c <= 122) {
			output += String.fromCharCode((c - 97 + key) % 26 + 97); // Lowercase
		} else {
			output += input.charAt(i); // Copy
		}
	}
	return output;
}
	
function getHPPCode(salt, input) {
    var saltedInput = salt + input;
   
    // Generate SHA-256 hash
    var sha256Hash = CryptoJS.SHA256(saltedInput);
 
    // Convert the hash to a hexadecimal string
    var hexHash = sha256Hash.toString(CryptoJS.enc.Hex);
	
    return hexHash;
}


function getInputCode2(input) {
	var output = 0;
	//alert(input + "-" + input.length);
	for (var i = 0; i < input.length; i++) {
		//alert(input.charCodeAt(i));
		if (input.charCodeAt(i) < 32) {
			continue;
		}

		var c = input.charCodeAt(i) + i;
		//alert("c = " + c);
		output += c;
		//alert("output = " + output);
	}
	return output;
}

function getHPPCode2(hppCode, inputCode) {
	var n = parseInt(inputCode);

	for (var i = 0; i < n; i++) {
		hppCode = encode(hppCode, 3);
	}
	return hppCode;
}
	/*function encodePasswordNEW_1(input, key) {
			
			var output = "";
			
			for (var i = 0; i < input.length; i++) {
				var c = input.charCodeAt(i);

				if (c >= 65 && c <= 90) {
					output += String.fromCharCode((c - 65 + key) % 26 + 65); // Uppercase
				} else if (c >= 97 && c <= 122) {
					output += String.fromCharCode((c - 97 + key) % 26 + 97); // Lowercase
				} else if (c >= 48 && c <= 57) {
					output += c + key; // Digits
				} else {
					output += input.charAt(i); // Copy
				}
			}
		
			var len = output.length;

			var inputCode2 = 0;
			inputCode2 = getInputCode2(output);
		
			inputCode2 = inputCode2 % 1000;
			
			var newhppCode = getHPPCode2(origHPPCode2, inputCode2);
		
			output = padZeroes(inputCode2, 3) + newhppCode.slice(0, 6) + output
					+ newhppCode.slice(6);
			
			return output;
		}
*/



function encodePasswordNEW_1(input, key) {
    var output = "";

    for (var i = 0; i < input.length; i++) {
        var c = input.charCodeAt(i);

        if (c >= 65 && c <= 90) {
            output += String.fromCharCode((c - 65 + key) % 26 + 65); // Uppercase
        } else if (c >= 97 && c <= 122) {
            output += String.fromCharCode((c - 97 + key) % 26 + 97); // Lowercase
        } else if (c >= 48 && c <= 57) {
            output += c + key; // Digits
        } else {
            output += input.charAt(i); // Copy
        }
    }

    var inputCode2 = getInputCode2(output);
    inputCode2 = inputCode2 % 1000;

    var newhppCode = getHPPCode2(origHPPCode2, inputCode2);

    output = padZeroes(inputCode2.toString(), 3) + newhppCode.slice(0, 6) + output + newhppCode.slice(6);

    return output;
}

function padZeroes(str, maxLen) {
    var len = str.length;
    var diff = maxLen - len;
    var padding = "";

    for (var i = 0; i < diff; i++) {
        padding += "0";
    }
    return padding + str;
}




		function padZeroes(str, maxLen) {

			var len = str.length;
			var diff = parseInt(maxLen) - parseInt(len);
			var padding = "";

			for (var i = 0; i < diff; i++) {
				padding += "0";
			}
			return padding + str;
		}



