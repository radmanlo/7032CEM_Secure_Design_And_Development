
document.addEventListener ('DOMContentLoaded', function () {
    let form = document.getElementById('create-appointment');
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let healthCenterJson = localStorage.getItem("healthCenterDto");
        let healthCenterDto = JSON.parse(healthCenterJson);
        let formData = new FormData(form);
        let jsonData ={
            date: formData.get("date"),
            time: formData.get("time")
        }
        if (formData.get("password") === formData.get("rPassword")){
            let jsonData = {
                name: formData.get("name"),
                email: formData.get("email"),
                password: formData.get("password")
            }
            fetch("http://localhost:6060/api/auth/hc/create",{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(jsonData),
            }).then(respond =>{
                if (respond.status === 201) {
                    localStorage.setItem("healthCenterDto", JSON.stringify(respond.json()));
                    alert(formData.get("name") + " Now You have account!")
                    location.href = `../../healthCenter/home/home.html`;
                }
                else if(respond.status === 302)
                    alert("This email address already register!")
                else
                    alert("Internal Server Error Please Again Later!")
            }).catch(error => {
                console.error('Error:', error);
            })
        }
        else
            alert("Password and Repeated Password are not same")
    })
})