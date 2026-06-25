const baseURL = 'http://localhost:8080';

export function enviarDados(endpoint, dados) {
    return fetch(`${baseURL}${endpoint}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(dados),
    })
    .then(response => response.json())
    .catch(error => console.error('Erro no POST:', error));
}

export function fazerLogin(email, senha) {
    const dadosFormulario = new URLSearchParams();
    dadosFormulario.append('email', email);
    dadosFormulario.append('senha', senha);

    return fetch(`${baseURL}/usuario/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: dadosFormulario
    })
    .then(response => {
        if (!response.ok) throw new Error('E-mail ou senha incorretos.');
        return response.json();
    })
    .catch(error => {
        console.error('Erro na requisição de login:', error);
        throw error;
    });
}
export function favoritarCep(cep, nome, usuarioId) {
    const url = `${baseURL}/favoritos?cep=${encodeURIComponent(cep)}&nome=${encodeURIComponent(nome)}&usuarioId=${usuarioId}`;

    return fetch(url, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) throw new Error('Erro ao favoritar o CEP no servidor.');
            return response.json();
        })
        .catch(error => {
            console.error('Erro na requisição de favoritar:', error);
            throw error;
        });
}