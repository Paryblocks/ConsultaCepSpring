import { fazerLogin } from './api.js';

const formulario = document.querySelector('form');

formulario.addEventListener('submit', function (event) {
    event.preventDefault();

    
    const emailDigitado = document.getElementById('Email').value;
    const senhaDigitada = document.getElementById('Senha').value;

    
    fazerLogin(emailDigitado, senhaDigitada)
        .then(usuarioLogado => {
            console.log('Login efetuado com sucesso:', usuarioLogado);

            sessionStorage.setItem('idUsuario', usuarioLogado.id);

            alert('Login realizado com sucesso!');
            
            window.location.href = 'index.html'; 
        })
        .catch(erro => {
            console.error(erro);
            alert('Não foi possível fazer o login. Verifique suas credenciais.');
        });
});