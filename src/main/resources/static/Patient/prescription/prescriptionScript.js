
let token = getCookie('jwt');
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
        window.location.href = "../../index.html";
    }
    else
        fetchApiPrescription();
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

function fetchApiPrescription(){
    let apiUrl = `http://localhost:6060/api/patient/getPrescription?patientEmail=${patientDto.email}`
    fetch(apiUrl, {
        method: "GET", // or "POST", "PUT", etc., depending on the HTTP method required by your API
        headers: {
            "Authorization": `Bearer ${token}` // Include the Bearer token in the "Authorization" header
        }
    }).then(response => {
        if (response.status === 302){
            response.json().then(data => {
                data.forEach((prescription)=>{
                    createPrescriptionCard(prescription)
                })
            })
        }
        else if (response.status === 400){
            alert("Bad Request")
        }
        else if (response.status === 403){
            alert("Your Token is expired or you are not sign in please sig in or register first!")
            window.location.href = "../../../index.html";
        }else {
            alert("Internal Server Error")
        }

    }).catch((error)=>{
        console.log("Error fetching data => ", error)
    })
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