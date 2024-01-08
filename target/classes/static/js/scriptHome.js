const linkLogin = document.querySelector('#login');
const nomeCookie = "usuarioID";
const inputPesquisa = document.querySelector('#inputPesquisa');
const listaPesquisaResult = document.querySelector('.listaProdutos');
const linkPerfil = document.querySelector('#perfil');
const botoesFiltro = document.querySelectorAll('.filtro-btn');

let estabs;


async function obterEstabs() {
	try {
		response = await fetch('/consultarEstabs');

		if (!response.ok) {
			erroMsg = await response.text();
			throw Error(erroMsg);
		}

		estabs = await response.json();

	} catch (error) {
		console.log(erroMsg);
	}

}

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

function buscarEstabsFiltrados(idTipo) {
	let estabsFiltrados = [];

	estabs.forEach(dado => {
		if (dado.tipoEstab.id === idTipo) {
			estabsFiltrados.push(dado);
		}
	})

	return estabsFiltrados.slice(0, 3);
}



async function popularEstabelecimentos(idTipo) {
	const services = document.querySelector('#services');

	services.innerHTML = '';

	if (idTipo === undefined || idTipo === 0) {

		estabs.slice(0, 3).forEach(dado => {
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
		const dados = buscarEstabsFiltrados(idTipo);

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

async function obterEstabsPorNome(nomeLocal) {

	let estabsFiltrados = [];

	estabsFiltrados = estabs.filter(dado => {
		const regex = new RegExp(nomeLocal, 'i');
		return regex.test(dado.nome);
	});

	return estabsFiltrados;

}


inputPesquisa.addEventListener('input', async (event) => {
	const nomeAtual = event.target.value

	if (nomeAtual) {
		const retorno = await obterEstabsPorNome(nomeAtual);

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

// teste


document.addEventListener('DOMContentLoaded', () => {
	obterEstabs()
		.then(() => {
			popularEstabelecimentos();
			verificarLogado();
			botaoAtivo();
			verificarAdm();
		})
		.catch(error => console.error(error));
})
