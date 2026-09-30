// Interface do simulador. Não faz cálculos: envia os dados para a API
// (POST /api/simular) e apenas exibe o que o servidor devolve.

const NOMES = ["A", "B", "C"];

// RF03: caso de referência (contexto_atividade.md, seção 2)
const EXEMPLO = {
    custoFixo: "3000",
    custoVariavel: "10",
    taxaPercentual: "10",
    beneficio: "Fim das ordens de serviço em papel, orçamento enviado ao dono do carro pelo celular, "
        + "histórico de cada veículo e lembretes automáticos de revisão.",
    propostas: [
        { preco: "40", clientes: "130" },
        { preco: "50", clientes: "100" },
        { preco: "70", clientes: "80" },
    ],
};

const moeda = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });
const decimal2 = new Intl.NumberFormat("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

const campo = (id) => document.getElementById(id);

// ---------- Leitura e preenchimento do formulário ----------

function lerEntrada() {
    return {
        custoFixo: campo("custoFixo").value,
        custoVariavel: campo("custoVariavel").value,
        taxaPercentual: campo("taxaPercentual").value,
        beneficio: campo("beneficio").value,
        propostas: NOMES.map((nome, i) => ({
            nome,
            preco: campo(`preco-${i}`).value,
            clientes: campo(`clientes-${i}`).value,
        })),
    };
}

function carregarExemplo() {
    campo("custoFixo").value = EXEMPLO.custoFixo;
    campo("custoVariavel").value = EXEMPLO.custoVariavel;
    campo("taxaPercentual").value = EXEMPLO.taxaPercentual;
    campo("beneficio").value = EXEMPLO.beneficio;
    EXEMPLO.propostas.forEach((p, i) => {
        campo(`preco-${i}`).value = p.preco;
        campo(`clientes-${i}`).value = p.clientes;
    });
    limparErros();
}

// ---------- Erros (RF09) ----------

function limparErros() {
    document.querySelectorAll("[data-erro]").forEach((span) => (span.textContent = ""));
    document.querySelectorAll("input.invalido").forEach((input) => input.classList.remove("invalido"));
    campo("erroGeral").textContent = "";
}

function mostrarErros(erros) {
    erros.forEach(({ campo: nomeCampo, mensagem }) => {
        const span = document.querySelector(`[data-erro="${nomeCampo}"]`);
        if (span) {
            span.textContent = mensagem;
            span.previousElementSibling?.classList.add("invalido");
        } else {
            campo("erroGeral").textContent = mensagem;
        }
    });
}

// ---------- Tabela comparativa (RF06) ----------

function textoMargem(r) {
    return r.margem === null ? "não se aplica" : `${decimal2.format(r.margem)}%`;
}

function textoEquilibrio(r) {
    if (r.equilibrio === null) {
        return "Sem equilíbrio: cada oficina aumenta o prejuízo";
    }
    return `${r.equilibrio} oficinas (${decimal2.format(r.equilibrioBruto)})`;
}

function textoFolga(r) {
    if (r.folga === null) {
        return "—";
    }
    return r.folga < 0 ? `${r.folga} oficinas (abaixo do equilíbrio)` : `${r.folga} oficinas`;
}

function mostrarTabela({ premissas, resultados }) {
    // Cada linha: [rótulo, função que gera o texto da célula, destacar?, marcar negativo?]
    const linhas = [
        ["Preço mensal", (r) => moeda.format(r.preco)],
        ["Oficinas estimadas", (r) => r.clientes],
        ["Receita", (r) => moeda.format(r.receita)],
        [`Tributos (${decimal2.format(premissas.taxaPercentual)}%)`, (r) => moeda.format(r.tributos)],
        ["Custo variável total", (r) => moeda.format(r.custoVariavelTotal)],
        ["Custo fixo", () => moeda.format(premissas.custoFixo)],
        ["Resultado mensal", (r) => moeda.format(r.resultado), true, (r) => r.resultado < 0],
        ["Margem", textoMargem, false, (r) => r.margem !== null && r.margem < 0],
        ["Contribuição unitária", (r) => moeda.format(r.contribuicaoUnitaria), false, (r) => r.contribuicaoUnitaria <= 0],
        ["Equilíbrio", textoEquilibrio, true],
        ["Folga sobre o equilíbrio", textoFolga, false, (r) => r.folga !== null && r.folga < 0],
    ];

    const corpo = campo("corpoTabela");
    corpo.innerHTML = "";
    for (const [rotulo, texto, destacar, negativo] of linhas) {
        const tr = document.createElement("tr");
        if (destacar) tr.classList.add("destaque");

        const th = document.createElement("td");
        th.textContent = rotulo;
        tr.appendChild(th);

        for (const r of resultados) {
            const td = document.createElement("td");
            td.textContent = texto(r);
            if (negativo && negativo(r)) td.classList.add("negativo");
            if (r.equilibrio === null && rotulo === "Equilíbrio") td.classList.add("quebra");
            tr.appendChild(td);
        }
        corpo.appendChild(tr);
    }
    campo("resultados").hidden = false;
}

// ---------- Gráfico (RF07) ----------

const CORES = ["#1f5fae", "#d97706", "#15803d"]; // A, B, C
let graficoAtual = null;

function mostrarGrafico({ eixoXMaximo, series }) {
    // Chart.js vem da CDN; sem internet, a tabela continua funcionando.
    if (typeof Chart === "undefined") {
        campo("erroGeral").textContent = "Gráfico indisponível: não foi possível carregar o Chart.js (sem internet?).";
        return;
    }

    const linhas = series.map((serie, i) => ({
        label: `Proposta ${serie.nome} (${moeda.format(serie.preco)})`,
        data: serie.pontos.map((p) => ({ x: p.clientes, y: p.resultado })),
        borderColor: CORES[i],
        backgroundColor: CORES[i],
        pointRadius: 3,
        tension: 0,
    }));
    linhas.push({
        label: "Resultado zero",
        data: [{ x: 0, y: 0 }, { x: eixoXMaximo, y: 0 }],
        borderColor: "#6b7280",
        borderDash: [6, 4],
        borderWidth: 1,
        pointRadius: 0,
    });

    if (graficoAtual) graficoAtual.destroy();
    graficoAtual = new Chart(campo("grafico"), {
        type: "line",
        data: { datasets: linhas },
        options: {
            maintainAspectRatio: false,
            animation: false,
            scales: {
                x: { type: "linear", min: 0, max: eixoXMaximo, title: { display: true, text: "Quantidade de oficinas" } },
                y: {
                    title: { display: true, text: "Resultado mensal (R$)" },
                    ticks: { callback: (valor) => moeda.format(valor) },
                },
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: (ctx) => `${ctx.dataset.label}: ${moeda.format(ctx.parsed.y)} com ${ctx.parsed.x} oficinas`,
                    },
                },
            },
        },
    });
}

// ---------- Cálculo via API ----------

async function calcular(evento) {
    evento.preventDefault();
    limparErros();
    campo("resultados").hidden = true;

    let resposta;
    try {
        resposta = await fetch("/api/simular", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(lerEntrada()),
        });
    } catch {
        campo("erroGeral").textContent = "Não foi possível falar com o servidor. Ele está rodando?";
        return;
    }

    const dados = await resposta.json();
    if (dados.erros && dados.erros.length > 0) {
        mostrarErros(dados.erros);
        return;
    }
    campo("interpretacao").textContent = dados.interpretacao;
    mostrarTabela(dados);
    mostrarGrafico(dados.grafico);
}

campo("btnExemplo").addEventListener("click", carregarExemplo);
campo("formulario").addEventListener("submit", calcular);
