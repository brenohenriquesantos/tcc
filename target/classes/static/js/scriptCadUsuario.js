//DOM
const btnProsseguir = document.querySelector("#btnProsseguir");
const cpf = document.querySelector("#cpf");
const email = document.querySelector("#email");
const senha = document.querySelector("#senha");
const data = document.querySelector("#data");
const nome = document.querySelector("#nome");
const senhaConfirmar = document.querySelector("#senhaConfirmar");
const copyright = document.querySelector("#copyright");
const filerInput = document.querySelector('#file-input');
const imgPrincipal = document.querySelector('#imgPrincipal');
let imagemData = "";
//DOM








//funções
function validarEmail(email) {
	var re = /\S+@\S+\.\S+/;
	return re.test(email);
}

//mascara cpf
$(document).ready(function() {
	$('#cpf').mask('000.000.000-00');
});

function validarSenha(senhaParaValidar) {
	if (senhaParaValidar.length < 8) {
		return false;
	} else {
		return true;
	}
}

function senhaIguais(senha1, senha2) {
	if (senha1 != senha2) {
		return false;
	} else {
		return true;
	}
}

function verificarCampos(event) {
	const camposForm = [cpf, email, senha, senhaConfirmar];

	var camposPreenchidos = true;

	camposForm.forEach(function(campo) {
		if (!campo.value.trim()) {
			camposPreenchidos = false;
		}
	});

	if (!camposPreenchidos) {
		event.preventDefault();
		alert("Preencha Todos os Campos!");
		return false;
	} else {
		return true;
	}
}

function validarImagem(dadoImagem) {
	if (dadoImagem === "" || !dadoImagem) {
		return false;
	}

	return true;
}


function verificarCPF(cpfParaVerificar) {
	fetch('/verificarCPF', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(`"${cpfParaVerificar}"`)
	})
		.then(response => {
			if (!response.ok) {
				return response.text()
					.then(data => {
						if (data.includes('vazio')) {
							return null;
						} else {
							alert(data)
						}
					});
			}
		})
}

function mostrarImgEscolhida(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();

	reader.onload = function(e) {
		imgPrincipal.src = e.target.result;
	}

	reader.readAsDataURL(file);
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
		const imagem64 = await imgToBase64(filerInput);
		return imagem64;
	} catch (error) {
		console.error(error.mesage);
		return null;
	}
}

async function enviarDados(){
	const imagem = await processarImagem();
	

	const dadosJuntos = {
		cpf: cpf.value,
		email: email.value,
		senha: senha.value,
		data: data.value,
		nome: nome.value,
		fotoBase64: imagem
	};

	const resposta = await fetch("/cadastrarUsuario", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(dadosJuntos)
	})
	
	if(!resposta.ok){
		const erroTexto = await resposta.text();
		alert(erroTexto);
	}
	
	const msgSucesso = await resposta.text();
	alert(msgSucesso);
	window.location.href = "/";
	
}


//Depois de carrregar todo o html
document.addEventListener("DOMContentLoaded", () => {
	var anoAtual = new Date().getFullYear();
	copyright.textContent = "© " + anoAtual + " ODIN. Todos os direitos reservados";
})


email.addEventListener("blur", function() {
	var verificar = validarEmail(email.value);
	var alertaMostrado = false;

	if (!verificar && !alertaMostrado) {
		email.value = "";
		alertaMostrado = true;
		alert("Por favor, insira um endereço de email válido.");
		alertaMostrado = false;
	}
});

senha.addEventListener("blur", function() {
	var retorno = validarSenha(senha.value);
	var alertaMostrado = false;

	if (!retorno && !alertaMostrado) {
		senha.value = "";
		alertaMostrado = true;
		alert("Por favor, insira uma senha valida!");
		alertaMostrado = false;

	}
});

senhaConfirmar.addEventListener("blur", function() {
	var retorno = senhaIguais(senha.value, senhaConfirmar.value);
	var alertaMostrado = false;

	if (!retorno && !alertaMostrado) {
		senha.value = "";
		senhaConfirmar.value = ""
		alertaMostrado = true;
		alert("As senhas devem ser iguais !");
		alertaMostrado = false;
	}
});

btnProsseguir.addEventListener("click", function(event) {
	event.preventDefault();
	var retorno = verificarCampos(event);
	if (retorno) {

		if (validarImagem(imgPrincipal.getAttribute('src'))) {
			enviarDados();
		} else {
			alert("Escolha uma foto !");
		}
	}
});

cpf.addEventListener("blur", function() {
	verificarCPF(cpf.value);
});


filerInput.addEventListener('change', function() {
	mostrarImgEscolhida(this);
})
