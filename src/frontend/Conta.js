document.addEventListener('DOMContentLoaded', () => {
    let idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
    let nomeUsuario = sessionStorage.getItem('nomeUsuario') || sessionStorage.getItem('nome');
    let emailUsuario = sessionStorage.getItem('emailUsuario') || sessionStorage.getItem('email');

    console.log("Investigando o sessionStorage:", { idUsuario, nomeUsuario, emailUsuario });

    if (!idUsuario || idUsuario === "null" || idUsuario === "undefined") {
        alert('Você precisa estar logado para acessar esta página!');
        window.location.href = 'Login.html';
        return;
    }

    const elementoNome = document.getElementById('textoNome');
    const elementoEmail = document.getElementById('textoEmail');

    if (elementoNome) elementoNome.textContent = nomeUsuario || "Nome não identificado";
    if (elementoEmail) elementoEmail.textContent = emailUsuario || "E-mail não identificado";

    const formNome = document.getElementById('formAtualizarNome');
    if (formNome) {
        formNome.addEventListener('submit', function (e) {
            e.preventDefault();
            const novoNome = document.getElementById('Nome').value.trim();

            if (!novoNome) return alert('Por favor, digite um nome válido.');

            fetch(`http://localhost:8080/usuario/atualizar/${idUsuario}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome: novoNome })
            })
            .then(response => {
                if (!response.ok) throw new Error('Erro ao atualizar o nome no servidor.');
                return response.json();
            })
            .then(usuarioAtualizado => {
                alert('Nome atualizado com sucesso!');
                sessionStorage.setItem('nomeUsuario', usuarioAtualizado.nome);
                window.location.reload();
            })
            .catch(erro => alert(erro.message));
        });
    }

    const formEmail = document.getElementById('formAtualizarEmail');
    if (formEmail) {
        formEmail.addEventListener('submit', function (e) {
            e.preventDefault();
            const novoEmail = document.getElementById('Email').value.trim();

            if (!novoEmail) return alert('Por favor, digite um e-mail válido.');

            fetch(`http://localhost:8080/usuario/atualizar/${idUsuario}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: novoEmail })
            })
            .then(response => {
                if (!response.ok) throw new Error('Erro ao atualizar o e-mail no servidor.');
                return response.json();
            })
            .then(usuarioAtualizado => {
                alert('E-mail atualizado com sucesso!');
                sessionStorage.setItem('emailUsuario', usuarioAtualizado.email);
                window.location.reload();
            })
            .catch(erro => alert(erro.message));
        });
    }

    const formSenha = document.getElementById('formAtualizarSenha');
    if (formSenha) {
        formSenha.addEventListener('submit', function (e) {
            e.preventDefault();
            const novaSenha = document.getElementById('Senha').value.trim();

            if (!novaSenha) return alert('Por favor, digite uma nova senha.');

            fetch(`http://localhost:8080/usuario/atualizar/${idUsuario}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ senha: novaSenha })
            })
            .then(response => {
                if (!response.ok) throw new Error('Erro ao atualizar a senha no servidor.');
                alert('Senha atualizada com sucesso!');
                document.getElementById('Senha').value = '';
            })
            .catch(erro => alert(erro.message));
        });
    }

    const botaoExcluir = document.getElementById('botaoExcluirConta');
    if (botaoExcluir) {
        botaoExcluir.addEventListener('click', function () {
            if (confirm('Tem certeza absoluta que deseja excluir sua conta? Esta ação não pode ser desfeita.')) {
                fetch(`http://localhost:8080/usuario/deletar/${idUsuario}`, { method: 'DELETE' })
                    .then(response => {
                        if (!response.ok) throw new Error('Erro ao deletar a conta.');
                        alert('Sua conta foi excluída.');
                        sessionStorage.clear();
                        window.location.href = 'Index.html';
                    })
                    .catch(erro => alert(erro.message));
            }
        });
    }

    const botaoLogout = document.getElementById('logout');
    if (botaoLogout) {
        botaoLogout.addEventListener('click', function (event) {
            event.preventDefault();
            sessionStorage.clear();
            localStorage.clear();
            alert('Adeus!');
            window.location.href = 'Login.html';
        });
    }
});