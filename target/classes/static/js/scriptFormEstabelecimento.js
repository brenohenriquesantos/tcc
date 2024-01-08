//DOOM

const imgEstabelecimento = document.querySelector('#imgEstabelecimento');
const titulo = document.querySelector('#titulo');
const descricao = document.querySelector('#descricao');
const estacionamento = document.querySelector('#estacionamento');
const rampa = document.querySelector('#rampa');
const banheiro = document.querySelector('#banheiro');
const btnEnviar = document.querySelector('#btnEnviar');
const message = document.querySelector('#message');
const numeroComentarios = document.querySelector('#numeroComentarios');
const comentarios = document.querySelector('#comentarios');
const map = L.map('map');

const textoLocal = document.querySelector('#textoLocal');
const diaHorario = document.querySelector('#diaHorario');
const horario = document.querySelector('#horario');
const telefone = document.querySelector('#telefone');
const linkLogin = document.querySelector('#login');
const linkPerfil = document.querySelector('#perfil');
const nomeCookie = "usuarioID";


let latitude = 0;
let longitude = 0;
let marker, circle, zoomed;

let estabs;
const listaPesquisaResult = document.querySelector('.listaProdutos');



map.on('click', (event) => {

	if (marker !== null) {
		map.removeLayer(marker);
	}

	marker = L.marker([event.latlng.lat, event.latlng.lng]).addTo(map);

	console.log(event.latlng.lat);
	console.log(event.latlng.lng);
	console.log(event);

})


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

async function obterEstabsPorNome(nomeLocal) {

	let estabsFiltrados = [];

	estabsFiltrados = estabs.filter(dado => {
		const regex = new RegExp(nomeLocal, 'i');
		return regex.test(dado.nome);
	});

	return estabsFiltrados;

}

function verificarCookie(nomeCookie) {
	var cookieValor = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));

	if (cookieValor) {
		return true;
	}

	return false;
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

function obterIdLogado() {
	const cookie = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));

	const id = cookie ? parseInt(cookie.split('=')[1]) : null;

	return id;
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



function obterIdUrl() {
	const url = window.location.search;

	var urlParametro = new URLSearchParams(url);

	var id = urlParametro.get('id');

	return id;
}

function adicionarMascaraTelefone(telefone) {
	if (!telefone) {
		return "";
	}

	let telefoneDD = "(" + telefone.substring(0, 2) + ") ";

	let telefoneSemDD = telefone.substring(2, 14);

	telefoneSemDD = telefoneSemDD.substring(0, 5) + '-' + telefoneSemDD.substring(4, 9);


	telefone = telefoneDD + telefoneSemDD;


	return telefone;
}


function limparCep(cep) {
	cep = cep.replace(/[^0-9]/g, '');

	return cep;
}


async function obterEstabelecimento(id) {
	let resposta = await fetch('/estabelecimento/consultar?id=' + id);

	try {
		if (!resposta.ok) {
			window.location.href = "/";
		} else {
			const dados = await resposta.json();
			return dados;
		}
	} catch (erro) {
		window.location.href = "/";
	}

}

function alterarBackgroundServices(dados) {
	if (dados.estacionamento_acessivel === 'S') {
		estacionamento.classList.add('hover-green');
	}

	if (dados.rampa_acessivel === 'S') {
		rampa.classList.add('hover-green');
	}

	if (dados.banheiro_acessivel === 'S') {
		banheiro.classList.add('hover-green');
	}
}

function obterValorCookie(nomeCookie) {
	var cookieString = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));

	if (!cookieString) {
		return null;

	}

	return cookieString.split('=')[1];
}

async function obterComentarios(idForm) {
	if (idForm === null) {
		throw new Error("Formulario vazio !");
	}

	const resposta = await fetch('/estabelecimento/comentarios?estabID=' + idForm);

	if (!resposta.ok) {
		throw new Error("Erro ao buscar os comentarios")
	}

	const listaComentarios = await resposta.json();
	return listaComentarios;
}

function criarCorpoComentario(comentario) {

	let divBody = document.createElement('div');
	divBody.classList.add('media-body');

	let h4 = document.createElement('h4');
	h4.classList.add('media-heading');
	h4.textContent = comentario.usr.nome;

	let p = document.createElement('p');
	p.textContent = comentario.texto;

	divBody.appendChild(h4);
	divBody.appendChild(p);


	let ul = document.createElement('ul');
	ul.classList.add("list-unstyled", "list-inline", "media-detail", "pull-left");


	let li = document.createElement('li');

	let i = document.createElement('i');
	i.classList.add("fa", "fa-calendar");



	li.appendChild(i);
	li.textContent = formatarData(comentario.data);

	ul.appendChild(li);

	divBody.appendChild(ul);

	return divBody;
}

function criarComentario(comentario) {

	let div = document.createElement('div');
	div.classList.add('media');

	let a = document.createElement('a');
	a.classList.add('pull-left');

	let img = document.createElement('img');
	img.src = "data:image/jpg;base64," + comentario.usr.fotoBase64;

	a.appendChild(img);

	div.appendChild(a);

	let divBody = criarCorpoComentario(comentario);

	div.appendChild(divBody);

	return div;

}

async function listarComentarios() {
	try {
		const idForm = obterIdUrl();

		const listaComentarios = await obterComentarios(idForm);

		numeroComentarios.textContent = listaComentarios.length + " Comentario(s)";

		listaComentarios.forEach(comentario => {

			let divComentario = criarComentario(comentario);
			comentarios.appendChild(divComentario);
		})

	} catch (Erro) {

		alert(Erro);
	}


}

async function carregarImgUsr() {

	if (verificarUsrLogado()) {

		const id = obterValorCookie('usuarioID');

		const resposta = await fetch('/usuario/consultar?id=' + id);

		if (resposta.ok) {

			const dados = await resposta.json();

			const imgUsr = document.querySelector('#imgUsr');

			imgUsr.src = "data:image/jpeg;base64," + dados.fotoBase64;
		}
	}


}

function verificarUsrLogado() {
	const idUsr = obterValorCookie('usuarioID');

	if (idUsr != null) {
		return true;
	}

	return false;
}

function formatarData(data) {
	const dataFormatada = moment(data);
	return dataFormatada.format('DD/MM/YYYY');
}

function preencherLocalTexto(dados) {
	const logradouro = dados.endereco.logradouro;
	const bairro = dados.endereco.bairro;
	const localidade = dados.endereco.localidade;
	const cepLocal = mascaraCep(dados.endereco.cep);

	textoLocal.textContent = logradouro + ', ' + bairro + ', ' + localidade + ', ' + cepLocal;
}

function mascaraCep(cep) {

	let cepQuebra1 = cep.substring(0, 2);
	let cepQuebra2 = cep.substring(2, 5);
	let cepQuebra3 = cep.substring(5, 8);

	cep = cepQuebra1 + '.' + cepQuebra2 + '-' + cepQuebra3;


	return cep;
}

function popularHorarioEstab(dados) {
	diaHorario.textContent = dados.estHorario.diaSemana;
	horario.textContent = dados.estHorario.horarioAbertura + ' ás ' + dados.estHorario.horarioFechamento;
	telefone.textContent = adicionarMascaraTelefone(dados.telefone);
}


document.addEventListener('DOMContentLoaded', async () => {

	obterEstabs();

	const id = obterIdUrl();

	verificarLogado();

	const dados = await obterEstabelecimento(id);

	titulo.textContent = dados.nome;
	descricao.textContent = dados.descricao;
	imgEstabelecimento.src = "data:image/jpeg;base64," + dados.fotoBase64;

	preencherLocalTexto(dados);

	popularHorarioEstab(dados);

	alterarBackgroundServices(dados);

	listarComentarios();

	carregarImgUsr();

	obterLatELong(dados.endereco.cep);

})


btnEnviar.addEventListener('click', async (event) => {
	event.preventDefault();
	const idForm = obterIdUrl();
	const idUsr = obterValorCookie('usuarioID');

	if (!idUsr) {
		alert("Faça login para comentar !");
		return;
	}

	const comentarioTexto = message.value;

	const dados = {
		"idUsuario": idUsr,
		"idEstabelecimento": idForm,
		"texto": comentarioTexto
	}

	try {
		const resposta = await fetch('/estabelecimento/comentar', {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(dados)
		})

		if (resposta.ok) {
			location.reload();
		} else {
			const txtErro = await resposta.text();
			alert(txtErro);
		}
	} catch (error) {
		const msgErro = await error.text();
		alert(msgErro);
	}




})

async function obterLatELong(cep) {

	cep = limparCep(cep);

	const urlGeo = 'https://nominatim.openstreetmap.org/search?format=json&postalcode=' + cep;

	const resposta = await fetch(urlGeo);

	const dados = await resposta.json();

	if (dados.length > 0) {
		latitude = parseFloat(dados[0].lat);
		longitude = parseFloat(dados[0].lon);

		map.setView([latitude, longitude], 15);

		marcadorMapa();
	}

}

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
	maxZoom: 19,
	attribution: '© OpenStreetMap'
}).addTo(map);



function marcadorMapa() {

	const lat = latitude;
	const lng = longitude;

	if (marker) {
		map.removeLayer(marker);
	}

	marker = L.marker([lat, lng]).addTo(map);
}































