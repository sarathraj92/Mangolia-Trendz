const form = document.getElementById('accountForm');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const email = document.getElementById('email');
const phone = document.getElementById('mobileNumber');


const setError = (element, message, e) => {
    console.log(element)
    e.preventDefault();
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success');
};
const setSuccess = element => {
    console.log(element)
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');
    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
};
const isValidEmail = email => {
    //const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const re =/[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}/;
    return re.test(String(email).toLowerCase());
}

function validateInputs(e) {
    setSuccess(firstName);
    setSuccess(lastName);
    setSuccess(email);
    setSuccess(phone);


    const firstNameValue = firstName.value.trim();
    const lastNameValue = lastName.value.trim();
    const phoneValue = phone.value.trim();
    const emailValue = email.value.trim();

    if(firstNameValue === '') {
        setError(firstName, 'Please enter first name', e);
        firstName.focus();
        return false;
    }
    else{
        setSuccess(firstName);
    }
    if(lastNameValue === '') {
        setError(lastName, 'Please enter last name', e);
        lastName.focus();
        return false;
    }
    else{
        setSuccess(lastName);
    }

    if(emailValue === '') {
        setError(email, 'Please enter email', e);
        email.focus();
        return false;
    } else if (!isValidEmail(emailValue)) {
        setError(email, 'Provide a valid email address', e);
        email.focus();
        return false;
    }
    else{
        setSuccess(email);
    }

    const phonenoRegex = /^\d{10}$/;
    if(phoneValue === ""){
        setError(phone, 'Enter phone number', e);
        phone.focus();
        return false;
    }
    else if(!phoneValue.match(phonenoRegex)){
        setError(phone, 'Enter valid number', e);
        phone.focus();
        return false;
    }
    else{
        setSuccess(phone);
    }


    return true;
}

form.addEventListener('submit', function(e) {
    console.log("hello");
    // e.preventDefault()

    if(validateInputs(e)){
        console.log("VALIDATION Success");
    }
})