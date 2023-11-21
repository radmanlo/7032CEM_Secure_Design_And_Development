let token = getCookie('jwt');
// jsonData = localStorage.getItem("patientDto");
let patientDto = JSON.parse(jsonData);
fetch(`http://localhost:6060/api/auth/isAuthenticated/patient`,
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
}).catch(error => {
    console.error('Error:', error);
});
fetchApiAppoPatient();

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

function fetchApiAppoPatient(){
    let apiUrlGet = `http://localhost:6060/api/appointment/patient/allAppointments?patientEmail=${patientDto.email}`
    fetch(apiUrlGet, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}` // Include the Bearer token in the "Authorization" header
        }
    }).then(response =>{
        if (response.status === 200){
            response.json().then(data =>{
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
            alert("Internal Server Error! Please tey again later")
        }
    }).catch((error)=>{
        console.log("Error fetching data => ", error)
    })
}

function createAppointmentCard(appointment){

    let pendingAppointmentCard = document.createElement('div');
    pendingAppointmentCard.classList.add('pending-appointment-card');
    let rejectedAppointmentCard = document.createElement('div');
    rejectedAppointmentCard.classList.add('reject-appointment-card');

    if (appointment.status === "PENDING" || appointment.status === "ACCEPTED"){
        pendingAppointmentCard.innerHTML =`
            <div class="field-label">Health Center:</div>
            <div class="field-value">${appointment.healthCenterDto.name}</div>
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <button class="cancel-button">Cancel Appointment</button>
        `;
        pendingAppointmentCard.querySelector(`.cancel-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure you want to Cancel this Appointment?");
            if (confirmation) {
                let updateAppo = {
                    id: appointment.id,
                    status: "CANCELED"
                }
                fetch(`http://localhost:6060/api/appointment/update`,
                    {
                        method: "PUT",
                        headers: {
                            "Authorization": `Bearer ${token}`, // Include the Bearer token in the "Authorization" header
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(updateAppo)
                    }
                ).then(response => {
                    if (response.status === 200) {
                        alert("Appointment is canceled successfully!")
                        let rejectContainer = document.getElementById("rejected-container")
                        let pendingContainer = document.getElementById("pending-container")
                        let canceledContainer = document.getElementById("canceled-container")
                        let acceptedContainer = document.getElementById("accepted-container")
                        rejectContainer.innerHTML = ``;
                        pendingContainer.innerHTML = ``;
                        canceledContainer.innerHTML = ``;
                        acceptedContainer.innerHTML = ``;
                        fetchApiAppoPatient()
                    } else if (response.status === 400) {
                        alert("Bad Request Status!")
                    }
                    else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../../index.html";
                    }else {
                        alert("Internal Server Error")
                    }
                }).catch(error => {
                    alert("ERROR IN CANCELLATION");
                    console.error('Error:', error);
                });
            }
        })
        if (appointment.status === "ACCEPTED"){
            let acceptContainer = document.getElementById('accepted-container')
            acceptContainer.appendChild(pendingAppointmentCard);
        }
        else{
            let pendingContainer = document.getElementById('pending-container')
            pendingContainer.appendChild(pendingAppointmentCard);
        }
    }
    else if (appointment.status === "CANCELED" || appointment.status === "REJECTED"){
        rejectedAppointmentCard.innerHTML =`
            <div class="field-label">Health Center:</div>
            <div class="field-value">${appointment.healthCenterDto.name}</div>
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
        `;
        if (appointment.status === "REJECTED"){
            let rejectContainer = document.getElementById('rejected-container')
            rejectContainer.appendChild(rejectedAppointmentCard);
        }
        else{
            let canceledContainer = document.getElementById('canceled-container')
            canceledContainer.appendChild(rejectedAppointmentCard);
        }

    }
}