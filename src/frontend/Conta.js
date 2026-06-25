import {getDados,  enviarDados } from './api.js'; 

const idUsuario = sessionStorage.getItem('idUsuario');

getDados(`/usuario/${idUsuario}`) 
    .then(usuario => {
        if (usuario) {
            document.getElementById('textoNome').textContent = usuario.nome;
            document.getElementById('textoEmail').textContent = usuario.email;
        }
    });

const formNome = document.getElementById('formAtualizarNome');
formNome.addEventListener('submit', function (e) {
    e.preventDefault();
    const novoNome = document.getElementById('Nome').value;

    const dados = { id: parseInt(idUsuario), nome: novoNome };
     
    enviarDados('/usuario/alterar-nome', dados)
        .then(() => {
            alert('Nome atualizado com sucesso!');
            window.location.reload(); 
        });
});

const formEmail = document.getElementById('formAtualizarEmail');
formEmail.addEventListener('submit', function (e) {
    e.preventDefault();
    const novoEmail = document.getElementById('Email').value;

    const dados = { id: parseInt(idUsuario), email: novoEmail };

    enviarDados('/usuario/alterar-email', dados)
        .then(() => {
            alert('E-mail atualizado com sucesso!');
            window.location.reload();
        });
});


const formSenha = document.getElementById('formAtualizarSenha');
formSenha.addEventListener('submit', function (e) {
    e.preventDefault();
    const novaSenha = document.getElementById('Senha').value;

    const dados = { id: parseInt(idUsuario), senha: novaSenha };

    enviarDados('/usuario/alterar-senha', dados)
        .then(() => {
            alert('Senha atualizada com sucesso!');
            document.getElementById('Senha').value = '';
        });
});

const botaoExcluir = document.getElementById('botaoExcluirConta');
botaoExcluir.addEventListener('click', function () {
    if (confirm('Tem certeza absoluta que deseja excluir sua conta? Esta ação não pode ser desfeita.')) {
        fetch(`http://localhost:8080/usuario/deletar/${idUsuario}`, { method: 'DELETE' })
            .then(() => {
                alert('Sua conta foi excluída.');
                sessionStorage.clear(); 
                window.location.href = 'Index.html'; 
            });
    }
});


document.addEventListener('DOMContentLoaded', () => {
    const botaoLogout = document.getElementById('logout');
    if (botaoLogout) {
        botaoLogout.addEventListener('click', function(event) {
            event.preventDefault();
            alert('Você tem que logar para poder fazer logout!');
        });
    }
});

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