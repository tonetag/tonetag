const textElement = document.getElementById('searchbox');

// Get all tags from /api/list
const search_terms = [];

fetch('/api/list')
    .then(response => response.json())
    .then(data => {
        search_terms.push(...data);
    }
);

function pickTextColour(bgColour) {
    let r, g, b, hsp;

    if (bgColour.match(/^rgb/)) {
        bgColour = bgColour.match(/rgba?\(([^)]+)\)/)[1];
        bgColour = bgColour.split(/ *, */).map(Number);
        r = bgColour[0];
        g = bgColour[1];
        b = bgColour[2];
    }
    const brightness = (r * 299 + g * 587 + b * 114) / 1000;
    return brightness < 125 ? '#fff' : '#000';
}

const cards = document.getElementsByClassName('card-header');
for (let i=0; i<cards.length; i++) {
    let card = cards[i];
    let bgColour = window.getComputedStyle(card).backgroundColor;
    card.style.color = pickTextColour(bgColour);
}

textElement.addEventListener('keydown', function(event) {
    const text = textElement.value;

    if (event.key === 'Enter') {
        event.preventDefault();

        if (text == '') {
            return;
        }
        for (let i=0; i<search_terms.length; i++) {
            if (text == search_terms[i].tag) {
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

    // Reorder list
    list = list.sort();
    if (list.indexOf(val) > -1) {
        list.splice(list.indexOf(val), 1);
        list.unshift(val);
    }


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