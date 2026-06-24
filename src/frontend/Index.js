// index.js

const idUsuario = sessionStorage.getItem('idUsuario');

// Form 1: CEP
const formCep = document.getElementById('formCep');
formCep.addEventListener('submit', function (e) {
    e.preventDefault();
    
    const cep = document.getElementById('Cep').value.trim();
    if (!cep) {
        alert('Digite um CEP válido.');
        return;
    }

    const url = idUsuario 
        ? `http://localhost:8080/cep/${cep}?usuarioId=${idUsuario}`
        : `http://localhost:8080/cep/${cep}`;

    // Alerta para sabermos que ele tentou enviar
    alert('Tentando conectar ao Java na URL: ' + url);

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('CEP não encontrado no servidor.');
            return response.json();
        })
        .then(data => {
            if (!data) {
                alert('O Java respondeu, mas o CEP não existe.');
                return;
            }
            
            // Limpa a tabela e adiciona a linha
            const corpoTabela = document.getElementById('corpoTabela');
            corpoTabela.innerHTML = ''; 
            adicionarLinhaTabela(data);
            alert('Sucesso! Os dados foram colocados na tabela.');
        })
        .catch(erro => {
            // Esse alerta vai estourar na tela dizendo exatamente o que quebrou!
            alert('ERRO DE CONEXÃO: Não foi possível falar com o Java. Detalhes: ' + erro.message);
        });
});

// Form 2: Endereço
const formEndereco = document.getElementById('formEndereco');
formEndereco.addEventListener('submit', function (e) {
    e.preventDefault();

    const uf = document.getElementById('UF').value.trim();
    const cidade = document.getElementById('Cidade').value.trim();
    const logradouro = document.getElementById('Logradouro').value.trim();

    if (!uf || !cidade || !logradouro) {
        alert('Preencha todos os campos do endereço (UF, Cidade e Logradouro).');
        return;
    }

    const cidadeUrl = encodeURIComponent(cidade);
    const logradouroUrl = encodeURIComponent(logradouro);

    const url = idUsuario
        ? `http://localhost:8080/cep/${uf}/${cidadeUrl}/${logradouroUrl}?usuarioId=${idUsuario}`
        : `http://localhost:8080/cep/${uf}/${cidadeUrl}/${logradouroUrl}`;

    fetch(url)
        .then(response => response.json())
        .then(listaCeps => {
            const corpoTabela = document.getElementById('corpoTabela');
            corpoTabela.innerHTML = '';

            if (!listaCeps || listaCeps.length === 0) {
                alert('Nenhum endereço encontrado.');
                return;
            }

            listaCeps.forEach(cepData => {
                adicionarLinhaTabela(cepData);
            });
            alert('Endereços carregados com sucesso!');
        })
        .catch(erro => alert('Erro no endereço: ' + erro.message));
});

// Função para colocar os dados na tabela
// Substitua a função antiga no final do index.js por esta:
function adicionarLinhaTabela(cepObjeto) {
    const corpoTabela = document.getElementById('corpoTabela');

    // Se por acaso o JS não achar o tbody, ele avisa
    if (!corpoTabela) {
        alert("Erro no HTML: Não foi encontrado o elemento com id='corpoTabela'");
        return;
    }

    const tr = document.createElement('tr');
    
    // Mapeando exatamente os campos que o seu Java devolve no JSON
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

    // Insere a nova linha no topo da tabela
    corpoTabela.appendChild(tr);
}