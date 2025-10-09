const xValues = [-4, -4, -3, -2, -1, 0, 1, 2, 4]
const minY = -5, maxY = 5
const minR = 1, maxR = 4

function setPossibleValues() {
    const xInput = document.getElementById("x");
    const yInput = document.getElementById("y");
    const rInput = document.getElementById("r");

    for (const x of xValues) {
        let option = document.createElement("option")
        option.value = x.toString()
        option.textContent = x.toString()
        xInput.appendChild(option);
    }

    yInput.placeholder = `${minY}..${maxY}`;
    yInput.title = `Value from ${minY} to ${maxY}`;
    yInput.min = minY
    yInput.max = maxY
    yInput.step = "any"

    rInput.placeholder = `${minR}..${maxR}`;
    rInput.title = `Value from ${minR} to ${maxR}`;
    rInput.min = minR
    rInput.max = maxR
    rInput.step = "any"
}

document.addEventListener("DOMContentLoaded", () => {
    setPossibleValues();
});