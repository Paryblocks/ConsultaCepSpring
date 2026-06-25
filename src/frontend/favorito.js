document.addEventListener('DOMContentLoaded', () => {
    carregarFavoritos();
    configurarLogout();
});

function carregarFavoritos() {
    const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');
    
    if (!idUsuario || idUsuario === "null" || idUsuario === "undefined") {
        alert('Você precisa estar logado para ver seus favoritos!');
        window.location.href = 'Login.html';
        return;
    }

    const url = `http://localhost:8080/favoritos/usuario/${idUsuario}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar favoritos do servidor.');
            return response.json();
        })
        .then(listaFavoritos => {
            renderizarTabelaFavoritos(listaFavoritos);
        })
        .catch(erro => console.error('Erro ao carregar os favoritos:', erro.message));
}

function renderizarTabelaFavoritos(favoritos) {
    const corpoTabela = document.getElementById('corpoFavoritos');
    if (!corpoTabela) return;

    corpoTabela.innerHTML = '';

    if (!favoritos || favoritos.length === 0) {
        corpoTabela.innerHTML = `
            <tr>
                <td colspan="3" style="text-align: center; color: gray; padding: 20px;">
                    Você ainda não possui nenhum CEP favoritado.
                </td>
            </tr>`;
        return;
    }

    favoritos.forEach(fav => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td><strong>${fav.nome || 'Sem nome'}</strong></td>
            <td>${fav.cep || 'N/A'}</td>
            <td>
                <button class="botao btn-deletar" style="background-color: #d9534f; color: white;"> Remover </button>
            </td>
        `;

        const btnDeletar = tr.querySelector('.btn-deletar');
        btnDeletar.addEventListener('click', () => {
            excluirFavorito(fav.cep, tr);
        });

        corpoTabela.appendChild(tr);
    });
}

function excluirFavorito(cep, linhaElemento) {
    const idUsuario = sessionStorage.getItem('idUsuario') || sessionStorage.getItem('usuarioId');

    if (!confirm(`Tem certeza que deseja remover o CEP ${cep} dos seus favoritos?`)) {
        return;
    }

    const url = `http://localhost:8080/favoritos?cep=${encodeURIComponent(cep)}&usuarioId=${idUsuario}`;

    fetch(url, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro ao remover o favorito no servidor.');
        
        alert('Favorito removido com sucesso!');
        linhaElemento.remove(); 

        const corpoTabela = document.getElementById('corpoFavoritos');
        if (corpoTabela && corpoTabela.children.length === 0) {
            corpoTabela.innerHTML = `
                <tr>
                    <td colspan="3" style="text-align: center; color: gray; padding: 20px;">
                        Você ainda não possui nenhum CEP favoritado.
                    </td>
                </tr>`;
        }
    })
    .catch(erro => alert('Não foi possível remover o favorito. Detalhes: ' + erro.message));
}

function configurarLogout() {
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
}