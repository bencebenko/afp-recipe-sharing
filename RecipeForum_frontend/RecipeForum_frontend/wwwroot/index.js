function getCookie(name) {
    var cookie = document.cookie;
    if (cookie.includes(name)) {
        return cookie;
    }
    else {
        return "";
    }
}