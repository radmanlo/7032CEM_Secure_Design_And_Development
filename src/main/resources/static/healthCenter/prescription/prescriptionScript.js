let token = getCookie('jwt');
let jsonData = localStorage.getItem("healthCenterDto");
let data = JSON.parse(jsonData);

fetch(`https://habsecurity.azurewebsites.net/api/auth/isAuthenticated/HC`,
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
    let form = document.getElementById("finding-patient");
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(form);
        fetchPatient(formData.get("email"))
    })
})

function fetchPatient(email){
    fetch(`https://habsecurity.azurewebsites.net/api/patient/getByEmail?email=${email}`,
        {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`, // Include the Bearer token in the "Authorization" header
            }
        }
    ).then(response => {
        if (response.status === 302) {
            response.json().then(patient => {
                let patientContainer = document.getElementById('patient-container')
                patientContainer.innerHTML='';
                let prescriptionContainer = document.getElementById('prescriptions-container')
                prescriptionContainer.innerHTML='';
                let updateContainer = document.getElementById('update-container')
                updateContainer.innerHTML ='';
                let createPrescriptionContainer = document.getElementById('create-prescription-container')
                createPrescriptionContainer.innerHTML='';
                createPatientCard(patient)
            })
        } else if (response.status === 404) {
            alert("Patient Not Found!")
        }else if (response.status === 403){
            alert("Your Token is expired or you are not sign in please sig in or register first!")
            window.location.href = "../../index.html";
        }else{
            alert("Internal Server Error!")
        }
    }).catch(error => {
        alert("ERROR IN CREATION");
        console.error('Error:', error);
    });
}

function createPatientCard(patient){
    let patientContainer1 = document.getElementById('patient-container')
    patientContainer1.innerHTML = ''
    let prescriptionContainer = document.getElementById('prescriptions-container')
    prescriptionContainer.innerHTML='';
    let updateContainer = document.getElementById('update-container')
    updateContainer.innerHTML ='';
    let createPrescriptionContainer = document.getElementById('create-prescription-container')
    createPrescriptionContainer.innerHTML='';
    let patientCard = document.createElement('div');
    patientCard.classList.add('patient-card');
    let prescriptionCard = document.createElement('div');
    prescriptionCard .classList.add('prescription-card');
    patientCard.innerHTML = `
        <div class="field-label">Patient Name:</div>
        <div class="field-value">${patient.firstName} ${patient.lastName}</div>
        <div class="field-label">Patient Allergies:</div>
        <div class="field-value">${patient.allergies}</div>
        <div class="field-label">Patient Diseases:</div>
        <div class="field-value">${patient.diseases}</div>
        <button class="past-prescription-button">Past Prescriptions</button>
        <button class="create-prescription-button">Create Prescription</button>
        <button class="update-allDis-button">Update Allergies and Diseases</button>
    `;
    patientCard.querySelector(`.past-prescription-button`).addEventListener('click', ()=> {
        event.preventDefault();
        let apiUrl = `https://habsecurity.azurewebsites.net/api/patient/getPrescription?patientEmail=${patient.email}`
        fetch(apiUrl, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}` // Include the Bearer token in the "Authorization" header
            }
        }).then(response => {
            if (response.status === 302){
                response.json().then(data =>{
                    let prescriptionContainer = document.getElementById('prescriptions-container')
                    prescriptionContainer.innerHTML='';
                    let updateContainer = document.getElementById('update-container')
                    updateContainer.innerHTML ='';
                    let createPrescriptionContainer = document.getElementById('create-prescription-container')
                    createPrescriptionContainer.innerHTML='';
                    prescriptionCard.innerHTML = `
                    <h1>Prescriptions</h1>
                `;
                    prescriptionContainer.appendChild(prescriptionCard);
                    data.forEach((prescription)=>{
                        createPrescriptionCard(prescription)
                    })
                })
            } else if (response.status === 403){
                alert("Your Token is expired or you are not sign in please sig in or register first!")
                window.location.href = "../../index.html";
            }
            else if (response.status === 404){
                alert("Bad Request!")
            }
            else {
                alert("Internal Server Error! Please Try again Later")
            }
        }).catch((error)=> {
            console.log("Error fetching data => ", error)
        })
    })
    patientCard.querySelector(`.create-prescription-button`).addEventListener('click', ()=> {
        event.preventDefault();
        let createPrescriptionCard = document.createElement('div');
        createPrescriptionCard.classList.add('prescription-card');
        let prescriptionContainer = document.getElementById('prescriptions-container')
        prescriptionContainer.innerHTML='';
        let updateContainer = document.getElementById('update-container')
        updateContainer.innerHTML ='';
        let createPrescriptionContainer = document.getElementById('create-prescription-container')
        createPrescriptionContainer.innerHTML='';
        createPrescriptionCard.innerHTML = `
            <label for="illness">illness:</label>
            <textarea id="illness" name="illness" rows="5" required></textarea>
            <label for="medicine">Medicine:</label>
            <textarea id="medicine" name="medicine" rows="5" required></textarea>
            <button class="submit-prescription-button">Submit Prescriptions</button>
        `;
        createPrescriptionCard.querySelector(`.submit-prescription-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure after that you cannot make change in prescription?");
            if (confirmation) {
                let formCreatePrescription = document.getElementById("create-prescription");
                let newPrescription = new FormData(formCreatePrescription);
                let prescription = {
                    date: new Date().toLocaleString(),
                    healthCenter:{
                        email: data.email,
                        name: data.name
                    },
                    illness: newPrescription.get('illness'),
                    medicine: newPrescription.get('medicine')
                }
                fetch(`https://habsecurity.azurewebsites.net/api/patient/addPrescription?patientEmail=${patient.email}`,
                    {
                        method: "POST",
                        headers: {
                            "Authorization": `Bearer ${token}`, // Include the Bearer token in the "Authorization" header
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(prescription)
                    }
                ).then(response =>{
                    if (response.status === 201){
                        alert("Prescription is created successfully!")
                        createPatientCard(patient)
                    }
                    else if(response.status === 400){
                        alert("Bad Request Status!")
                    }
                    else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else{
                        alert("Internal Server Error!")
                    }
                }).catch((error)=>{
                    console.log("Error fetching data => ", error)
                })
            }
        })
        createPrescriptionContainer = document.getElementById('create-prescription-container')
        createPrescriptionContainer.appendChild(createPrescriptionCard);
    })
    patientCard.querySelector(`.update-allDis-button`).addEventListener('click', ()=> {
        event.preventDefault();
        let updateAllDisCard = document.createElement('div');
        updateAllDisCard.classList.add('prescription-card');
        let prescriptionContainer = document.getElementById('prescriptions-container')
        prescriptionContainer.innerHTML='';
        let updateContainer = document.getElementById('update-container')
        updateContainer.innerHTML ='';
        let createPrescriptionContainer = document.getElementById('create-prescription-container')
        createPrescriptionContainer.innerHTML='';
        updateAllDisCard.innerHTML = `
            <label for="allergies">Allergies:</label>
            <textarea id="allergies" name="allergies" rows="5" required></textarea>
            <label for="diseases">Diseases:</label>
            <textarea id="diseases" name="diseases" rows="5" required></textarea>
            <button class="submit-alldis-button">Submit Prescriptions</button>
        `;
        updateAllDisCard.querySelector(`.submit-alldis-button`).addEventListener('click', ()=> {
            event.preventDefault();
            let confirmation = window.confirm("Are you sure after that you cannot make change in prescription?");
            if (confirmation) {
                let formUpdate = document.getElementById("update");
                let newAllDis = new FormData(formUpdate);
                let updatedPatient ={
                    email: patient.email,
                    allergies: newAllDis.get("allergies"),
                    diseases: newAllDis.get("diseases")
                }
                fetch(`https://habsecurity.azurewebsites.net/api/patient/update/allDis`,
                    {
                        method: "PUT",
                        headers: {
                            "Authorization": `Bearer ${token}`, // Include the Bearer token in the "Authorization" header
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(updatedPatient)
                    }
                ).then(response =>{
                    if (response.status === 202){
                        alert("Allergies and Diseases are updated!")
                        fetchPatient(patient.email)
                    }
                    else if(response.status === 404){
                        alert("Bad Request Status!")
                    }
                    else if (response.status === 403){
                        alert("Your Token is expired or you are not sign in please sig in or register first!")
                        window.location.href = "../../index.html";
                    }
                    else{
                        alert("Internal Server Error!")
                    }
                }).catch((error)=>{
                    console.log("Error fetching data => ", error)
                })
            }
        })
        updateContainer = document.getElementById('update-container')
        updateContainer.appendChild(updateAllDisCard);
    })
    let patientContainer = document.getElementById('patient-container')
    patientContainer.appendChild(patientCard);
}

function createPrescriptionCard (prescription) {

    let prescriptionCard = document.createElement('div');
    prescriptionCard.classList.add('prescription-card');
    prescriptionCard.innerHTML =`
        <div class="field-label">Health Center Name:</div>
        <div class="field-value">${prescription.healthCenter.name}</div>
        <div class="field-label">Date and time:</div>
        <div class="field-value">${prescription.date}</div>
        <div class="field-label">illness:</div>
        <div class="field-value">${prescription.illness}</div>
        <div class="field-label">Medicens:</div>
        <div class="field-value">${prescription.medicine}</div>
    `;
    let prescriptionContainer = document.getElementById('prescriptions-container')
    prescriptionContainer.appendChild(prescriptionCard);
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

