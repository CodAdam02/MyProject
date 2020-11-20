
//rember before you try and open the website use http://localhost:8081/client/index.html
function getUsersList() {
    //debugger;
    console.log("Invoked getUsersList()");     //console.log your BFF for debugging client side - also use debugger statement
    const url = "/Users/list/";    		// API method on web server will be in Users class, method list
    fetch(url, {
        method: "GET",				//Get method
    }).then(response => {
        return response.json();                 //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert (pop up window)
        } else {
            formatUsersList(response);          //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}


function formatUsersList(myJSONArray){
    let dataHTML = "";
    for (let item of myJSONArray) {
        dataHTML += "<tr><td>" + item.UserName + "<td><td>" + item.Email + "<tr><td>" + item.Password + "<td><td>" + item.UserID + "<td><td>" + item.Admin  ;
    }
    document.getElementById("UsersTable").innerHTML = dataHTML;
}

//addUser function to add a user to the database
function addUser() {
    console.log("Invoked AddUser()");
    const formData = new FormData(document.getElementById('InputUserDetails'));
    let url = "/Users/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            window.open("/client/welcome.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                  //in the client folder called welcome.html
    });
}



