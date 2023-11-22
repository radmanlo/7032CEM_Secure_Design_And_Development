document.addEventListener ('DOMContentLoaded', function () {
    let form = document.getElementById('register-hc-form');
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(form);
        if (formData.get("password") === formData.get("rPassword")){
            let jsonData = {
                name: formData.get("name"),
                email: formData.get("email"),
                password: formData.get("password")
            }
            fetch("https://habsecurity.azurewebsites.net/api/auth/hc/create",{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(jsonData),
            }).then(respond =>{
                if (respond.status === 201) {
                    respond.json().then(value => {
                        localStorage.setItem("healthCenterDto", JSON.stringify(value));
                    })
                    alert(formData.get("name") + " Now You have account!")
                    location.href = `../../healthCenter/home/home.html`;
                }
                else if(respond.status === 302)
                    alert("This email address is already registered!")
                else if (respond.status === 400){
                    alert("This email address is already registered!")
                }
                else
                    alert("Internal Server Error! Please try again later")
            }).catch(error => {
                console.error('Error:', error);
            })
        }
        else
            alert("Password and Repeated Password are not same")
    })
})