const form = document.getElementById("data-form");
const resultTable = document.getElementById("result-table");
const errorMsg = document.getElementById("error");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const x = document.getElementById("x").value;
    const y = document.getElementById("y").value;
    const r = document.getElementById("r").value;

    const point = { x, y, r };
    let data = {};

    try {
        errorMsg.hidden = true;

        const response = await fetch("/api", { // http://localhost:52000/fcgi-bin/web-lab1.jar instead of /api on helios
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify(point),
        });
        
        if (!response.ok) {
            data = await response.json();
            throw new Error("Server error");
        }

        data = await response.json();
    } catch (error) {
        // console.error(error);
        errorMsg.textContent = "Error! " + (data?.error ?? "Unknown error");
        errorMsg.hidden = false;
    }

    const tr = resultTable.insertRow(1);
    const td = (v) => {
        const td = document.createElement("td");
        td.innerText = v;
        return td;
    };

    tr.appendChild(td(data.x ?? x));
    tr.appendChild(td(data.y ?? y));
    tr.appendChild(td(data.r ?? r));
    tr.appendChild(td(data.currentTime ?? new Date().toLocaleTimeString()));
    tr.appendChild(td(data.executionTime ?? "—"));
    // tr.appendChild(td(data.result ?? "ERROR"));

    let result;
    if (!["OK", "MISS", "ERROR"].includes(data.result?.toUpperCase())) {
        result = "ERROR";
    } else {
        result = data.result.toUpperCase();
    }


    const resultTd = td(result);
    if (result === "OK") {
        resultTd.style.color = "lime";
    } else if (result === "MISS") {
        resultTd.style.color = "red";
    } else {
        resultTd.style.color = "yellow";
    }
    tr.appendChild(resultTd);

    if (result !== "ERROR") {
        drawPoint(Number(data.x), Number(data.y), Number(data.r), result === "OK");
    }
});
