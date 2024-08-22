 var states = [
      "Andhra Pradesh",
      "Arunachal Pradesh",
      "Assam",
      "Bihar",
      "Chhattisgarh",
      "Goa",
      "Gujarat",
      "Haryana",
      "Himachal Pradesh",
      "Jharkhand",
      "Karnataka",
      "Kerala",
      "Madhya Pradesh",
      "Maharashtra",
      "Manipur",
      "Meghalaya",
      "Mizoram",
      "Nagaland", 
      "Odisha", 
      "Punjab", 
      "Rajasthan", 
      "Sikkim", 
      "Tamil Nadu", 
      "Telangana",
      "Tripura", 
      "Uttar Pradesh", 
      "Uttarakhand", 
      "West Bengal"
    ];

    var unionTerritories = [
      "Andaman and Nicobar Islands", 
      "Chandigarh",
      "Dadra and Nagar Haveli and Daman and Diu", 
      "Jammu and Kashmir", 
      "Ladakh", 
      "Lakshadweep", 
      "Delhi", 
      "Puducherry"
    ];

    var stateDropdown = document.getElementById("State");

    // Function to populate the State dropdown
    function populateStates() {
      stateDropdown.innerHTML = '<option value="">Select state or union territory</option>';

      // Group states
      stateDropdown.innerHTML += '<optgroup label="States">';
      for (var i = 0; i < states.length; i++) {
        stateDropdown.innerHTML += '<option value="' + states[i] + '">' + states[i] + '</option>';
      }
      stateDropdown.innerHTML += '</optgroup>';

      // Group Union Territories
      stateDropdown.innerHTML += '<optgroup label="Union Territories">';
      for (var i = 0; i < unionTerritories.length; i++) {
        stateDropdown.innerHTML += '<option value="' + unionTerritories[i] + '">' + unionTerritories[i] + '</option>';
      }
      stateDropdown.innerHTML += '</optgroup>';
    }

    // Call the function to populate the dropdown on page load
    populateStates();