var textElement = document.getElementById('searchbox');

function goto() {
    const text = textElement.value;
    window.location.href = '/' + text;
}

textElement.addEventListener('keyup', function(event) {
    if (event.key === 'Enter') {
        goto();
    }
})