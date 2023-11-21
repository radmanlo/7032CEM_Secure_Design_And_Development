
document.addEventListener ('DOMContentLoaded', function () {

    const form = document.getElementById('signIn-form');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(form);

        fetch('http://localhost:6060/api/auth/patient?email=' + formData.get('email')
            +'&password='+formData.get('password')
        ).then(respond =>{
            if (respond.status === 302){
                let data;
                respond.json().then((res)=>{
                    data = {
                        firstName: res.firstName,
                        lastName: res.lastName,
                        email: res.email
                    }
                    localStorage.setItem("patientDto", JSON.stringify(data));
                    location.href = `../../Patient/home/home.html`;
                })
            }
            else if (respond.status === 404)
                alert("Username or password is wrong!");
            else if (respond.status === 500)
                alert("Username or password is wrong!");
            else
                alert("Sorry there is a problem in Server please try again later!")

        }).then( data => {
            console.log('API Response:', data);
        }).catch(error => {
            console.log("error ==>> " + error);
        })
    })
})