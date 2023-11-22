
let token = getCookie('jwt');
// let jsonData = localStorage.getItem("patientDto");
let patientDto = JSON.parse(jsonData);
jsonData = localStorage.getItem("healthCenterDto");
let healthCenterDto = JSON.parse(jsonData);
fetch(`https://habsecurity.azurewebsites.net/api/auth/isAuthenticated/patient`,
    {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
        },
    }
).then(response => {
    if (response.status === 403){
        alert("Your Token is expired or you are not sign in please sig in or register first!")
        window.location.href = "../../../index.html";
    }
    else
        fetchApiAppointmentsHC();
}).catch(error => {
    console.error('Error:', error);
});


function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function fetchApiAppointmentsHC(){
    let apiUrlGet = `https://habsecurity.azurewebsites.net/appointment/hc/allAppointments?hcEmail=${healthCenterDto.email}`
    fetch(apiUrlGet, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}` // Include the Bearer token in the "Authorization" header
        }
    }).then(response => {
        if (response.status === 302){
            response.json().then(data => {
                data.forEach((appointment)=>{
                    createAppointmentCard(appointment)
                })
            })
        }
        else if (response.status === 403){
            alert("Your Token is expired or you are not sign in please sig in or register first!")
            window.location.href = "../../../index.html";
        }
        else {
            alert("Internal Server Error! Please try again later")
        }
    }).catch((error)=>{
        console.log("Error fetching data => ", error)
    })
}

function createAppointmentCard(appointment){
    let emptyAppointmentCard = document.createElement('div');
    emptyAppointmentCard.classList.add('empty-appointment-card');
    if (appointment.status === "EMPTY"){
        emptyAppointmentCard.innerHTML =`
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <button class="reserve-button">Reserve Appointment</button>
        `;
        emptyAppointmentCard.querySelector(`.reserve-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure you want to reserve this block?");
            if (confirmation) {
                let newAppo = {
                    id : appointment.id,
                    patientDto:{
                        email: patientDto.email,
                        firstName: patientDto.firstName,
                        lastName: patientDto.lastName
                    },
                    status: "PENDING"
                }
                fetch(`https://habsecurity.azurewebsites.net/api/appointment/update`,
                    {
                        method: "PUT",
                        headers: {
                            'Authorization': `Bearer ${token}`,// Include the Bearer token in the "Authorization" header
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(newAppo)
                    }
                ).then(response =>{
                    if (response.status === 200){
                        alert("Appointment Block is Reserved Successfully!")
                        let pendingContainer = document.getElementById('empty-container')
                        pendingContainer.innerHTML = '';
                        fetchApiAppointmentsHC()
                    }
                    else if (response.status === 400){
                        alert("Bad Request!")
                    } else if (response.status === 403) {
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../../index.html";
                    }
                    else
                        alert("Internal Server Error! Please try again later")
                }).catch((error) => {
                    alert("Deleted Successfully")
                    console.log("ERROR => " + error)
                })
            }
        })
        let pendingContainer = document.getElementById('empty-container')
        pendingContainer.appendChild(emptyAppointmentCard);
    }
}
