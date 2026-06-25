
let dadosAtuaisTabela = [];

function carregarHistorico() {
    const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
    
    if (!idUsuario || idUsuario === "null" || idUsuario === "undefined") {
        return;
    }

    const url = `http://localhost:8080/cep/historico/${idUsuario}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar histórico do servidor.');
            return response.json();
        })
        .then(listaHistorico => {
            dadosAtuaisTabela = listaHistorico || [];
            aplicarOrdenacaoERenderizar();
        })
        .catch(erro => console.error('Erro ao carregar o histórico:', erro.message));
}


function aplicarOrdenacaoERenderizar() {
    const criterio = document.getElementById('ordenacaoHistorico').value;
    const corpoTabela = document.getElementById('corpoTabela');
    corpoTabela.innerHTML = '';

    if (!dadosAtuaisTabela || dadosAtuaisTabela.length === 0) return;
    let listaParaExibir = [...dadosAtuaisTabela];
    if (criterio === 'antigo') {
        listaParaExibir.reverse();
    }
    listaParaExibir.forEach(cepData => {
        adicionarLinhaTabela(cepData);
    });
}
document.addEventListener('DOMContentLoaded', carregarHistorico);

document.addEventListener('DOMContentLoaded', () => {
    const seletorOrdem = document.getElementById('ordenacaoHistorico');
    if (seletorOrdem) {
        seletorOrdem.addEventListener('change', aplicarOrdenacaoERenderizar);
    }
});




const formCep = document.getElementById('formCep');
formCep.addEventListener('submit', function (e) {
    e.preventDefault();
    
    const cephifen = document.getElementById('Cep').value.trim();
    cep = cephifen.replace("-", ""); 
    if (cep.length!=8)
    {
        alert('Digite um CEP válido.');
        return;
    }

    const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
    const idValido = (idUsuario && idUsuario !== "null") ? idUsuario : null;
    const url = idValido 
        ? `http://localhost:8080/cep/${cep}?usuarioId=${idValido}`
        : `http://localhost:8080/cep/${cep}`;

    fetch(url)
        .then(response => {
            if (response.status === 404) throw new Error('CEP inexistente.');
            if (!response.ok) throw new Error('Erro no servidor.');
            return response.json();
        })
        .then(data => {
            alert('Sucesso! CEP processado.');
            if (idUsuario) {
                carregarHistorico();
            } else {
                const corpoTabela = document.getElementById('corpoTabela');
                corpoTabela.innerHTML = ''; 
                adicionarLinhaTabela(data);
            }
        })
        .catch(erro => {
            alert('Não foi possível concluir essa ação! Detalhes: ' + erro.message);
        });
});


const formEndereco = document.getElementById('formEndereco');
formEndereco.addEventListener('submit', function (e) {
    e.preventDefault();

    const uf = document.getElementById('UF').value.trim();
    const city = document.getElementById('Cidade').value.trim();
    const logr = document.getElementById('Logradouro').value.trim();

    if (!uf || !city || !logr) {
        alert('Preencha todos os campos do endereço (UF, Cidade e Logradouro).');
        return;
    }

    const cidadeUrl = encodeURIComponent(city);
    const logradouroUrl = encodeURIComponent(logr);
    const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');

    const url = idUsuario
        ? `http://localhost:8080/cep/${uf}/${cidadeUrl}/${logradouroUrl}?usuarioId=${idUsuario}`
        : `http://localhost:8080/cep/${uf}/${cidadeUrl}/${logradouroUrl}`;

    fetch(url)
        .then(response => {
            if (response.status === 404) throw new Error('Nenhum endereço encontrado.');
            if (!response.ok) throw new Error('Erro no servidor.');
            return response.json();
        })
        .then(listaCeps => {
            alert('Endereços carregados com sucesso!');
            if (idUsuario) {
                carregarHistorico();
            } else {
                const corpoTabela = document.getElementById('corpoTabela');
                corpoTabela.innerHTML = '';
                listaCeps.forEach(cepData => adicionarLinhaTabela(cepData));
            }
        })
        .catch(erro => alert('Não foi possível concluir essa ação! Detalhes: ' + erro.message));
});

function adicionarLinhaTabela(cepObjeto) {
    const corpoTabela = document.getElementById('corpoTabela');

    if (!corpoTabela) {
        alert("Erro no HTML: Não foi encontrado o elemento com id='corpoTabela'");
        return;
    }

    const tr = document.createElement('tr');

    tr.innerHTML = `
        <td>${cepObjeto.cep || 'N/A'}</td>
        <td>${cepObjeto.logradouro || 'N/A'}</td>
        <td>${cepObjeto.complemento || ''}</td>
        <td>${cepObjeto.bairro || 'N/A'}</td>
        <td>${cepObjeto.localidade || cepObjeto.cidade || 'N/A'}</td>
        <td>${cepObjeto.uf || 'N/A'}</td>
        <td>${cepObjeto.estado || 'N/A'}</td>
        <td>${cepObjeto.regiao || 'N/A'}</td>
        <td>${cepObjeto.ddd || 'N/A'}</td>
        <td>
            <button class="botao"> Deletar </button>
        </td>
        <td>
            <button class="dourado botao"> Favoritar </button>
        </td>
    `;

    corpoTabela.appendChild(tr);
}

document.addEventListener('DOMContentLoaded', () => {
    const botaoLogout = document.getElementById('logout');
    if (botaoLogout) {
        botaoLogout.addEventListener('click', function (event) {
            event.preventDefault();
            
            const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
            if (idUsuario && idUsuario !== "null" && idUsuario !== "undefined") {
                sessionStorage.clear();
                localStorage.clear();
                
                alert('Adeus!');
                window.location.href = this.href; 
            } else {
                sessionStorage.clear();
                localStorage.clear();
                alert('Você não está logado!');
                window.location.href = 'Login.html';
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const btnLimpar = document.getElementById('btnLimparHistorico');
    
    if (btnLimpar) {
        btnLimpar.addEventListener('click', function () {
            const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');

            if (!idUsuario || idUsuario === "null" || idUsuario === "undefined") {
                alert('Você não está logado para possuir um histórico!');
                return;
            }

            if (!confirm('Tem certeza que deseja apagar todo o seu histórico de pesquisas? Essa ação não pode ser desfeita.')) {
                return;
            }
            const url = `http://localhost:8080/cep/historico/delete/${idUsuario}`;

            fetch(url, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) throw new Error('Erro ao limpar o histórico no servidor.');
                
                alert('Histórico limpo com sucesso!');
                dadosAtuaisTabela = [];
                const corpoTabela = document.getElementById('corpoTabela');
                if (corpoTabela) corpoTabela.innerHTML = '';
            })
            .catch(erro => {
                alert('Não foi possível limpar o histórico. Detalhes: ' + erro.message);
            });
        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const btnRelatorio = document.getElementById('btnGerarRelatorio');

    if (btnRelatorio) {
        btnRelatorio.addEventListener('click', function () {
            const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
            if (!idUsuario || idUsuario === "null" || idUsuario === "undefined") {
                alert('Você não está logado para gerar um relatório!');
                return;
            }

            const url = `http://localhost:8080/cep/relatorio/${idUsuario}`;

            alert('Preparando o seu relatório... O download começará em instantes.');
            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error('Erro ao gerar o relatório no servidor.');
                    return response.blob();
                })
                .then(blob => {
                    const urlArquivo = window.URL.createObjectURL(blob);
                    const linkTemporario = document.createElement('a');
                    linkTemporario.href = urlArquivo;
                    linkTemporario.download = `relatorio-ceps-${idUsuario}.csv`; 
                    document.body.appendChild(linkTemporario);
                    linkTemporario.click(); 
                    document.body.removeChild(linkTemporario);
                    window.URL.revokeObjectURL(urlArquivo);
                })
                .catch(erro => {
                    alert('Não foi possível gerar o relatório. Detalhes: ' + erro.message);
                });
        });
    }
});

