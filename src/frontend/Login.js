import { fazerLogin } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    const idUsuario = sessionStorage.getItem('idUsuario');

    if (idUsuario && idUsuario !== "null" && idUsuario !== "undefined") {
        alert('Você já está logado!');
        window.location.href = 'Index.html';
        return;
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

    const formulario = document.querySelector('form');

    if (formulario) {
        formulario.addEventListener('submit', function (event) {
            event.preventDefault();

            const emailDigitado = document.getElementById('Email').value;
            const senhaDigitada = document.getElementById('Senha').value;

            fazerLogin(emailDigitado, senhaDigitada)
                .then(usuarioLogado => {
                    if (usuarioLogado && usuarioLogado.id) {
                        sessionStorage.setItem('idUsuario', usuarioLogado.id);
                        alert('Login realizado com sucesso!');
                        window.location.href = 'Index.html';
                    } else {
                        alert('Erro: Problema no servidor');
                    }
                })
                .catch(erro => {
                    console.error(erro);
                    alert('Não foi possível fazer o login. Verifique suas credenciais.');
                });
        });
    }
});