import { enviarDados } from './api.js'; 

const formulario = document.getElementById('formCadastro');

formulario.addEventListener('submit', function (event) {
    event.preventDefault(); 

    const nomeDigitado = document.getElementById('Nome').value;
    const emailDigitado = document.getElementById('Email').value;
    const senhaDigitada = document.getElementById('Senha').value;

    if (!nomeDigitado.trim() || !emailDigitado.trim() || !senhaDigitada.trim()) {
        alert('Por favor, preencha todos os campos!');
        return; 
    }

    const dadosUsuario = {
        nome: nomeDigitado,
        email: emailDigitado,
        senha: senhaDigitada
    };

    enviarDados('/usuario/cadastro', dadosUsuario)
        .then(resposta => {
            console.log('Resposta do Java:', resposta);
            alert('Cadastro realizado com sucesso!');
            window.location.href = 'Login.html'; 
        })
        .catch(erro => {
            console.error('Erro ao cadastrar:', erro);
            alert('Erro ao realizar o cadastro. O e-mail já pode estar em uso.');
        });
});