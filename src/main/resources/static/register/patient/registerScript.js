document.addEventListener ('DOMContentLoaded', function () {
    let form = document.getElementById('register-form');
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(form);
        if (formData.get("password") === formData.get("rPassword")){
            if (formData.get("consent") != null) {
                let jsonData = {
                    firstName: formData.get("firstName"),
                    lastName: formData.get("lastName"),
                    email: formData.get("email"),
                    password: formData.get("password"),
                    allergies: formData.get("allergies"),
                    diseases: formData.get("diseases"),
                    consent: formData.get("consent   http://localhost:6060/api/auth/patient/create") === 1
                }
                fetch("api/auth/patient/", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(jsonData),
                }).then(respond => {
                    if (respond.status === 201) {
                        respond.json().then(value => {
                            localStorage.setItem("patientDto", JSON.stringify(value));
                            // console.log("Send to storage =>> " + JSON.stringify(value))
                        })
                        alert(formData.get("firstName") + " Now You have account!")
                        location.href = `../../Patient/home/home.html`;
                    } else if (respond.status === 400) {
                        alert("This email address already register!")
                    } else {
                        alert("Internal Server Error Please Again Later!")
                    }
                }).catch(error => {
                    console.error('Error:', error);
                })
            }
            else{
                alert("Please agree with term and condition!")
            }
        }
        else
            alert("Password and Repeated Password are not same")
    })
})