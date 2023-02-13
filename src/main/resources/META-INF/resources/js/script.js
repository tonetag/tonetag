const textElement = document.getElementById('searchbox');

// Get all tags from /api/list
const search_terms = [];

fetch('/api/list')
    .then(response => response.json())
    .then(data => {
        search_terms.push(...data);
    }
);

textElement.addEventListener('keyup', function(event) {
    const text = textElement.value;

    if (event.key === 'Enter') {
        if (text == '') {
            return;
        }
        for (let i=0; i<search_terms.length; i++) {
            if (text == search_terms[i].tag) {
                console.log('Redirecting to ' + text);
                window.location.href = '/' + text;
                return;
            }
        }
    }
})

function autocompleteMatch(input) {
    if (input == '') {
        return [];
    }
    const reg = new RegExp(input);
    return search_terms.filter(function(term) {
        if (term.tag.match(reg)) {
            return term;
        }
    });
}

function showResults(val) {
    let res = document.getElementById("result");

    if (val == '') {
        res.setAttribute('style', 'display: none');
        return;
    } else {
        res.setAttribute('style', 'display: block');
    }
    res.innerHTML = '';
    let list = [];
    let terms = autocompleteMatch(val);
    for (let i=0; i<terms.length; i++) {
        list.push(terms[i].tag);
    }

    // Reorder list by relevance

    list.sort(function(a, b) {
        return ('' + a).localeCompare(b);
    });

    let result = '';
    for (let i=0; i<list.length; i++) {
        result += '<a href="/' + list[i] + '">' + '<li>' + list[i] + '</li>' + '</a>';

    }

    res.innerHTML = '<ul>' + result + '</ul>';

    if (list == '') {
        res.innerHTML = '<ul><li>No results</li></ul>';
    }


    if (list == '') {
        res.innerHTML = '<ul><li>No results</li></ul>';
    }

}