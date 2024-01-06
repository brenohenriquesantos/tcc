const linkLogin = document.querySelector('#login');
const nomeCookie = "usuarioID";
const inputPesquisa = document.querySelector('#inputPesquisa');
const listaPesquisaResult = document.querySelector('.listaProdutos');
const linkPerfil = document.querySelector('#perfil');
const botoesFiltro = document.querySelectorAll('.filtro-btn');

function verificarCookie(nomeCookie) {
	var cookieValor = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));

	if (cookieValor) {
		return true;
	}

	return false;
}

function obterIdLogado() {
	const cookie = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));

	const id = cookie ? parseInt(cookie.split('=')[1]) : null;

	return id;
}

function deleteCookie(nomeCookie) {
	fetch('/deslogar', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: nomeCookie
	}
	)
}


function botaoAtivo() {
	const botoes = document.querySelectorAll('#filtros button');


	botoes.forEach(function(botao) {
		botao.addEventListener('click', function() {
			botoes.forEach(function(b) {
				b.classList.remove('ativo');
			});
			botao.classList.add('ativo');
		});
	});

}
function verificarLogado() {

	if (verificarCookie(nomeCookie)) {
		linkLogin.textContent = 'Deslogar'
		linkPerfil.style = 'display:block'
		linkPerfil.href = '/usuario/perfil/?id=' + obterIdLogado();

	} else {
		linkPerfil.style = 'display:none'
		linkLogin.textContent = 'Login'
	}
}



linkLogin.addEventListener('click', () => {
	if (verificarCookie(nomeCookie)) {
		deleteCookie(nomeCookie);
	}

	window.location.href = "/login"

})

async function buscarEstabsFiltrados(idTipo) {
	try {
		let resposta = await fetch('/estabelecimento/acessadosFiltrados',
			{
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: idTipo,
			});

		if (!resposta.ok) {
			resposta = await resposta.text();
			throw new Error(resposta);
		}

		const dados = await resposta.json();

		return dados;
	} catch (erro) {
		console.log(erro.message);
	}
}

async function buscarEstabs() {
	try {
		const response = await fetch('/estabelecimento/acessados');

		if (!response.ok) {
			throw new Error(response);
		}

		const dados = response.json();

		return dados;
	} catch (error) {
		const erroMsg = await error.text();
		console.log(erroMsg);
	}
}

async function popularEstabelecimentos(idTipo) {
	const services = document.querySelector('#services');

	// Limpar o conteúdo anterior removendo todos os elementos filhos
	services.innerHTML = '';

	if (idTipo === undefined) {
		const dados = await buscarEstabs();

		dados.forEach(dado => {
			const container = document.createElement('div');
			container.classList.add('container', 'px-4');

			const row = document.createElement('div');
			row.classList.add('row', 'gx-4', 'justify-content-center');

			const imagemCol = document.createElement('div');
			imagemCol.classList.add('col-lg-4');

			const link = document.createElement('a');
			link.href = '/estabelecimento/?id=' + dado.id;
			link.classList.add('linkID');

			const imagem = document.createElement('img');
			imagem.src = "data:image/jpeg;base64," + dado.fotoBase64;
			imagem.alt = 'Imagem';
			imagem.classList.add('img-fluid');

			const descricaoCol = document.createElement('div');
			descricaoCol.classList.add('col-lg-6');
			descricaoCol.id = 'sobre';

			const titulo = document.createElement('h2');
			titulo.textContent = dado.nome;

			const descricao = document.createElement('p');
			descricao.textContent = dado.descricao;

			// Montar a estrutura hierárquica dos elementos
			services.appendChild(container);
			container.appendChild(row);
			row.appendChild(imagemCol);
			imagemCol.appendChild(link);
			link.appendChild(imagem);
			row.appendChild(descricaoCol);
			descricaoCol.appendChild(titulo);
			descricaoCol.appendChild(descricao);
		});
	} else {
		const dados = await buscarEstabsFiltrados(idTipo);

		dados.forEach(dado => {
			const container = document.createElement('div');
			container.classList.add('container', 'px-4');

			const row = document.createElement('div');
			row.classList.add('row', 'gx-4', 'justify-content-center');

			const imagemCol = document.createElement('div');
			imagemCol.classList.add('col-lg-4');

			const link = document.createElement('a');
			link.href = '/estabelecimento/?id=' + dado.id;
			link.classList.add('linkID');

			const imagem = document.createElement('img');
			imagem.src = "data:image/jpeg;base64," + dado.fotoBase64;
			imagem.alt = 'Imagem';
			imagem.classList.add('img-fluid');

			const descricaoCol = document.createElement('div');
			descricaoCol.classList.add('col-lg-6');
			descricaoCol.id = 'sobre';

			const titulo = document.createElement('h2');
			titulo.textContent = dado.nome;

			const descricao = document.createElement('p');
			descricao.textContent = dado.descricao;

			// Montar a estrutura hierárquica dos elementos
			services.appendChild(container);
			container.appendChild(row);
			row.appendChild(imagemCol);
			imagemCol.appendChild(link);
			link.appendChild(imagem);
			row.appendChild(descricaoCol);
			descricaoCol.appendChild(titulo);
			descricaoCol.appendChild(descricao);
		});
	}
}


botoesFiltro.forEach(botao => {
	botao.addEventListener('click', () => {
		const tipoFiltro = botao.getAttribute('data-tipo');
		const numeroFiltro = parseInt(tipoFiltro, 10);

		popularEstabelecimentos(numeroFiltro);
	})
})




function verificarAdm() {
	const isAdm = sessionStorage.getItem('isAdm');
}

async function obterEstabs(nomeLocal) {

	try {
		const resposta = await fetch('/consultarEstabNome', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: nomeLocal
		});

		if (resposta.ok) {
			const dados = await resposta.json();

			return dados;
		}

		return null;
	} catch (error) {
		console.log('Erro ao consultar o estabelecimento com  o valor informado');
	}



}


inputPesquisa.addEventListener('input', async (event) => {
	const nomeAtual = event.target.value

	if (nomeAtual) {
		const retorno = await obterEstabs(nomeAtual);

		if (retorno != null && retorno.length > 0) {

			listaPesquisaResult.style = 'display:block';

			const ul = document.querySelector('#lista');

			ul.innerHTML = "";

			ul.style = 'display:block';

			for (i = 0; i < retorno.length; i++) {
				const li = document.createElement("li");

				li.innerHTML = `
					<a href="/estabelecimento/?id=${retorno[i].id}">
							<img width="80px" src="data:image/jpeg;base64,${retorno[i].fotoBase64}" alt="">
							<span class="item-name">${retorno[i].nome}</span>
					</a>
		`;

				ul.appendChild(li);
			}
		} else {
			listaPesquisaResult.style = 'display:none';
		}
	} else {
		listaPesquisaResult.style = 'display:none';
	}

})


document.addEventListener('DOMContentLoaded', () => {
	popularEstabelecimentos();

	verificarLogado()
	
	botaoAtivo()
	
	verificarAdm();
})
