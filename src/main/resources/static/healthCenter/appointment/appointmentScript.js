let jsonData = localStorage.getItem("healthCenterDto");
let data = JSON.parse(jsonData);
let token = getCookie('jwt');
// console.log("data => " + data)

fetch(`http://localhost:6060/api/auth/isAuthenticated/HC`,
    {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
        },
    }
).then(response => {
    if (response.status === 403){
        alert("Your Token is expired or you are not sign in please sig in or register first!")
        window.location.href = "../../index.html";
    }
}).catch(error => {
    console.error('Error:', error);
});

document.addEventListener ('DOMContentLoaded', function () {
    let form = document.getElementById("create-appointment");
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let confirmation = window.confirm("Are you sure you want to create this Appointment?");
        if (confirmation) {
            let formData = new FormData(form);
            let newAppo = {
                healthCenterDto: {
                    email: data.email,
                    name: data.name
                },
                date: formData.get("date"),
                time: formData.get("time")
            }
            console.log(newAppo)
            fetch(`http://localhost:6060/api/appointment/create`,
                {
                    method: "POST",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(newAppo)
                }
            ).then(response => {
                if (response.status === 201) {
                    alert("Appointment Slot is Created")
                    fetchApiAppointment()
                } else if (response.status === 400) {
                    alert("You have already an empty appointment block with this time and date")
                } else if (response.status === 403){
                    alert("Your Token is expired or you are not sign in please sig in or register first!")
                    window.location.href = "../../index.html";
                }
                else {
                    alert("Internal Server Error! Please try again later")
                }
            }).catch(error => {
                alert("ERROR IN CREATION");
                console.error('Error:', error);
            });
        }
    })
})

fetchApiAppointment()

function fetchApiAppointment(){
    let apiUrlGet = `http://localhost:6060/api/appointment/hc/allAppointments?hcEmail=${data.email}`
    fetch(apiUrlGet, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    }).then(response => {
        if (response.status === 302){
            response.json().then(data =>{
                data.forEach((appointment)=>{
                    createAppointmentCard(appointment)
                })
            })
        }else if (response.status === 403){
            alert("Your Token is expired or you are not sign in please sig in or register first!")
            window.location.href = "../../index.html";
        }
        else {
            alert("Internal Server Error! Please try again later")
        }
    })
}

function createAppointmentCard(appointment){

    let emptyAppointmentCard = document.createElement('div');
    emptyAppointmentCard.classList.add('empty-appointment-card');
    let acceptedAppointmentCard = document.createElement('div');
    acceptedAppointmentCard.classList.add('accepted-appointment-card');
    let pendingAppointmentCard = document.createElement('div');
    pendingAppointmentCard.classList.add('pending-appointment-card');
    let rejectedAppointmentCard = document.createElement('div');
    rejectedAppointmentCard.classList.add('rejected-appointment-card');
    let canceledAppointmentCard = document.createElement('div');
    canceledAppointmentCard.classList.add('rejected-appointment-card');

    if (appointment.status === "EMPTY"){
        emptyAppointmentCard.innerHTML =`
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <button class="delete-button">Delete Appointment</button>
        `;
        emptyAppointmentCard.querySelector(`.delete-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure you want to Delete this Appointment?");
            if (confirmation) {
                fetch(`http://localhost:6060/api/appointment/delete?appoId=${appointment.id}`,
                    {
                        method: "DELETE",
                        headers: {
                            'Authorization': `Bearer ${token}`,// Include the Bearer token in the "Authorization" header
                            'Content-Type': 'application/json'
                        }
                    }
                ).then(response =>{
                    if (response.status === 200){
                        alert("Appointment is deleted successfully")
                        let acceptContainer = document.getElementById("accepted-container")
                        let rejectContainer = document.getElementById("rejected-container")
                        let emptyContainer = document.getElementById("empty-container")
                        let pendingContainer = document.getElementById("pending-container")
                        let cancelContainer = document.getElementById("canceled-container")
                        acceptContainer.innerHTML =``;
                        rejectContainer.innerHTML=``;
                        emptyContainer.innerHTML=``;
                        pendingContainer.innerHTML=``;
                        cancelContainer.innerHTML=``;
                        fetchApiAppointment()
                    }
                    else if (response.status === 400){
                        alert("Bad Request Status!")
                    }else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else {
                        alert("Internal Server Error!")
                    }
                }).catch(error => {
                    alert("ERROR IN DELETION");
                    console.error('Error:', error);
                })
            }
        })
    }
    else if (appointment.status === "ACCEPTED"){
        acceptedAppointmentCard.innerHTML =`
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <div class="field-label">Patient Info:</div>
            <div class="field-value">${appointment.patientDto.email}</div>
            <div class="field-value">${appointment.patientDto.firstName} ${appointment.patientDto.lastName}</div>
            <button class="cancel-button">Cancel Appointment</button>
        `;
        acceptedAppointmentCard.querySelector(`.cancel-button`).addEventListener('click', ()=> {
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
                ).then(response =>{
                    if (response.status === 200){
                        alert("Appointment is canceled successfully!")
                        let acceptContainer = document.getElementById("accepted-container")
                        let rejectContainer = document.getElementById("rejected-container")
                        let emptyContainer = document.getElementById("empty-container")
                        let pendingContainer = document.getElementById("pending-container")
                        let cancelContainer = document.getElementById("canceled-container")
                        acceptContainer.innerHTML =``;
                        rejectContainer.innerHTML=``;
                        emptyContainer.innerHTML=``;
                        pendingContainer.innerHTML=``;
                        cancelContainer.innerHTML=``;
                        fetchApiAppointment()
                    }
                    else if (response.status === 400){
                        alert("Bad Request Status!")
                    }else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else{
                        alert("Internal Server Error")
                    }
                }).catch(error => {
                    alert("ERROR IN CANCELLATION");
                    console.error('Error:', error);
                });
            }
        })
    }
    else if (appointment.status === "PENDING") {
        pendingAppointmentCard.innerHTML = `
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <div class="field-label">Patient Info:</div>
            <div class="field-value">${appointment.patientDto.email}</div>
            <div class="field-value">${appointment.patientDto.firstName} ${appointment.patientDto.lastName}</div>
            <button class="accept-button">Accept Appointment</button>
            <button class="reject-button">Reject Appointment</button>
        `;
        pendingAppointmentCard.querySelector(`.accept-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure you want to Accept this Appointment?");
            if (confirmation) {
                let updateAppo = {
                    id: appointment.id,
                    status: "ACCEPTED"
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
                ).then(response =>{
                    if (response.status === 200){
                        alert("Appointment is accepted successfully")
                        let acceptContainer = document.getElementById("accepted-container")
                        let rejectContainer = document.getElementById("rejected-container")
                        let emptyContainer = document.getElementById("empty-container")
                        let pendingContainer = document.getElementById("pending-container")
                        let cancelContainer = document.getElementById("canceled-container")
                        acceptContainer.innerHTML =``;
                        rejectContainer.innerHTML=``;
                        emptyContainer.innerHTML=``;
                        pendingContainer.innerHTML=``;
                        cancelContainer.innerHTML=``;
                        fetchApiAppointment()
                    }
                    else if(response.status === 400){
                        alert("Bad Request Status!")
                    }
                    else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else {
                        alert("Internal Server error!")
                    }
                }).catch(error => {
                    alert("ERROR IN ACCEPTANCE");
                    console.error('Error:', error);
                });
            }
        })
        pendingAppointmentCard.querySelector(`.reject-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure you want to Reject this Appointment?");
            if (confirmation) {
                let updateAppo = {
                    id: appointment.id,
                    status: "REJECTED"
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
                ).then(response =>{
                    if (response.status === 200){
                        alert("Appointment is rejected successfully")
                        let acceptContainer = document.getElementById("accepted-container")
                        let rejectContainer = document.getElementById("rejected-container")
                        let emptyContainer = document.getElementById("empty-container")
                        let pendingContainer = document.getElementById("pending-container")
                        let cancelContainer = document.getElementById("canceled-container")
                        acceptContainer.innerHTML =``;
                        rejectContainer.innerHTML=``;
                        emptyContainer.innerHTML=``;
                        pendingContainer.innerHTML=``;
                        cancelContainer.innerHTML=``;
                        fetchApiAppointment()
                    }
                    else if (response.status === 400){
                        alert("Bad Request Status!")
                    }
                    else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else {
                        alert("Internal Server Error!")
                    }
                }).catch(error => {
                    alert("ERROR IN ACCEPTANCE");
                    console.error('Error:', error);
                })
            }
        })
    }
    else if (appointment.status === "REJECTED"){
        rejectedAppointmentCard.innerHTML = `
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <div class="field-label">Patient Info:</div>
            <div class="field-value">${appointment.patientDto.email}</div>
            <div class="field-value">${appointment.patientDto.firstName} ${appointment.patientDto.lastName}</div>
        `;
    }
    else if (appointment.status === "CANCELED"){
        canceledAppointmentCard.innerHTML = `
            <div class="field-label">Appointment date:</div>
            <div class="field-value">${appointment.date}</div>
            <div class="field-label">Appointment time:</div>
            <div class="field-value">${appointment.time}</div>
            <div class="field-label">Patient Info:</div>
            <div class="field-value">${appointment.patientDto.email}</div>
            <div class="field-value">${appointment.patientDto.firstName} ${appointment.patientDto.lastName}</div>
        `;
    }
    if (appointment.status === "ACCEPTED") {
        let acceptContainer = document.getElementById('accepted-container')
        acceptContainer.appendChild(acceptedAppointmentCard);
    }
    else if (appointment.status === "REJECTED"){
        let rejectedContainer = document.getElementById('rejected-container')
        rejectedContainer.appendChild(rejectedAppointmentCard);
    }
    else if (appointment.status === "PENDING"){
        let pendingContainer = document.getElementById('pending-container')
        pendingContainer.appendChild(pendingAppointmentCard);
    }
    else if (appointment.status === "EMPTY"){
        let emptyContainer = document.getElementById('empty-container')
        emptyContainer.appendChild(emptyAppointmentCard);
    }
    else if(appointment.status === "CANCELED"){
        let canceledContainer = document.getElementById('canceled-container')
        canceledContainer.appendChild(canceledAppointmentCard);
    }
}

function getCookie(name) {
    const cookieValue = document.cookie
        .split('; ')
        .find(cookie => cookie.startsWith(name + '='));

    if (cookieValue) {
        return cookieValue.split('=')[1];
    }

    return null;
}
