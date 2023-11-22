

let apiUrl = "https://habsecurity.azurewebsites.net/api/healthCenter/getAll"
let token = getCookie('jwt');
// console.log("token => " + token);

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
        fetchApiHealthCenter();
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

function fetchApiHealthCenter(){
    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}` // Include the Bearer token in the "Authorization" header
        }
    }).then(response => {
        if (response.status === 302){
            response.json().then(data => {
                data.forEach((healthCenter)=>{
                    createHealthCenterCard(healthCenter)
                })
            })
        }
        else if (response.status === 403){
            alert("Your Token is expired or you are not sign in please sig in or register first!")
            window.location.href = "../../../index.html";
        }
        else {
            alert("Internal Server Error! Please Try again later")
        }
    }).catch((error)=>{
        console.log("Error fetching data => ", error)
    })
}

function createHealthCenterCard(healthCenter){
    let HCCard = document.createElement('div');
    HCCard.classList.add('healthCenters-card');
    HCCard.innerHTML =`
        <div class="field-label">Health Center Name:</div>
        <div class="field-value">${healthCenter.name}</div>
        <button class="apply-button">Make Appointment</button>
    `;
    HCCard.querySelector(`.apply-button`).addEventListener('click', ()=> {
        event.preventDefault();
        let healthCenterDto = {
            email: healthCenter.email,
            name: healthCenter.name
        }
        localStorage.setItem("healthCenterDto", JSON.stringify(healthCenterDto));
        location.href = `../makeAppo/makeAppo.html`;
    })
    let HCContainer = document.getElementById('healthCenters-container')
    HCContainer.appendChild(HCCard);

}
