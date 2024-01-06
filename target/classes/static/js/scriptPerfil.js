const uploadInput = document.querySelector('#uploadInput');

function previewImage() {
	const input = document.getElementById('uploadInput');
	const preview = document.getElementById('preview');

	const file = input.files[0];
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			preview.src = e.target.result;
		};
		reader.readAsDataURL(file);
	}
}

function verificarLogado() {
	const cookieValor = document.cookie.split(';').find(row => row.trim().startsWith(nomeCookie + '='));
	if (cookieValor) {
		linkLogin.textContent = 'Deslogar';
	} else {
		window.location = '/login';
	}
}

function obterIdUrl() {
	const url = window.location.search;

	var urlParametro = new URLSearchParams(url);

	const id = urlParametro.get('id');

	return id;
}

async function popularPerfil() {
	const id = obterIdUrl();

	const response = await fetch('/usuario/consultar?id=' + id);

	if (response.ok) {
		const dados = await response.json();

		const imagem = document.querySelector('#preview');
		imagem.src = "data:image/jpg;base64," + dados.fotoBase64;

		const cpf = document.querySelector('#cpf');
		cpf.value = dados.cpf;

		const nome = document.querySelector('#nome');
		nome.value = dados.nome;

		const email = document.querySelector('#email');
		email.value = dados.email;

	}


}

function bloquearCampos() {
	const perfil = document.querySelector('#perfil');

	const inputs = perfil.getElementsByTagName('input');

	for (const inputAtual of inputs) {
		if (inputAtual.type == 'file') {
			inputAtual.setAttribute('disabled', 'true');
		} else {
			inputAtual.setAttribute('readonly', 'true');
		}

	}
}

function voltarHome() {
	window.location = '/';
}



function editarPerfil() {
	const perfil = document.querySelector('#perfil');
	const iconeEdit = document.querySelector('#iconeEdit');

	var classesAtuais = iconeEdit.className;
	var iconeLapis = classesAtuais.indexOf('fa-pen-to-square') !== -1;

	if (iconeLapis) {

		iconeEdit.classList.remove('fa-pen-to-square');
		iconeEdit.classList.add('fa-xmark');
	} else {
		iconeEdit.classList.remove('fa-xmark');
		iconeEdit.classList.add('fa-pen-to-square');
	}



	const inputs = perfil.getElementsByTagName('input');

	for (const inputAtual of inputs) {
		if (inputAtual.hasAttribute('readonly')) {
			inputAtual.removeAttribute('readonly');
			inputAtual.style.background = 'white';

		} else if (inputAtual.hasAttribute('disabled')) {
			inputAtual.removeAttribute('disabled');

		} else {

			if (inputAtual.type == 'file') {
				inputAtual.setAttribute('disabled', 'true');
			} else {
				inputAtual.setAttribute('readonly', 'true');
				inputAtual.style.background = '#cfc4c4';
			}

		}
	}

}

function imgToBase64(imagem) {
	return new Promise((resolve, reject) => {
		const arquivo = imagem.files[0];

		if (!arquivo) {
			reject(new Error("Nenhum arquivo fornecido."));
			return;
		}

		const reader = new FileReader();

		reader.onloadend = function() {
			const img = new Image();
			img.src = reader.result;

			img.onload = function() {
				const canvas = document.createElement('canvas');
				const canvasContexto = canvas.getContext('2d');

				const larguraImg = 1366;
				const alturaImg = 768;

				canvas.width = larguraImg;
				canvas.height = alturaImg;

				canvasContexto.drawImage(img, 0, 0, larguraImg, alturaImg);

				try {
					const imagem64 = canvas.toDataURL('image/jpeg', 0.8);
					resolve(imagem64);
				} catch (error) {
					reject(new Error("Erro ao processar imagem"));
				}


			}

			img.onerror = function() {
				reject(new Error("Erro ao carregar a imagem"));
			}

		}

		reader.onerror = function() {
			reject(new Error("Erro na leitura do arquivo."));
		}

		reader.readAsDataURL(arquivo);
	})
}

async function processarImagem() {
	try {
		const imagem64 = await imgToBase64(uploadInput);
		return imagem64;
	} catch (error) {
		console.error(error.mesage);
		return null;
	}
}

async function atualizarPerfil() {
	try {
		
		let imagem = "";
		if(!uploadInput.files.length === 0){
			imagem = await processarImagem();
		}else{
			imagem = preview.src;
		}
		

		if (!verificarCampos()) {
			alert('Preencha todos os campos!');
			return null;
		}

		const dados = {
			id: obterIdUrl(),
			cpf: document.querySelector('#cpf').value,
			nome: document.querySelector('#nome').value,
			email: document.querySelector('#email').value,
			fotoBase64: imagem
		};

		const resposta = await fetch('/usuario/atualizar', {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(dados)
		});

		if (resposta.ok) {
			const retorno = await resposta.text();
			alert(retorno);
		} else {
			const erroMsg = await resposta.text();
			console.log(erroMsg);
		}
	} catch (error) {
		console.error(error.message);
	}
}


function verificarCampos() {
	const divPai = document.querySelector('#perfil');

	const inputs = divPai.getElementsByTagName('input');

	for (const input of inputs) {

		if (input.type == 'file') {
			continue;
		}

		if (input.value == null || input.value == '') {
			return false;
		}

	}

	return true;
}

document.addEventListener('DOMContentLoaded', async () => {

	verificarLogado();

	popularPerfil();

	bloquearCampos();

})

