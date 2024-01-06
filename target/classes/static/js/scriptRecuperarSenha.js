//DOOM

const cpf = document.querySelector('#cpf');
const email = document.querySelector('#email');
const btnProsseguir = document.querySelector('#btnProsseguir');
const erroMsg = document.querySelector('#erroMsg');
const form_principal = document.querySelector('#form_principal');
const form_secundario = document.querySelector('#form_secundario');
const codigo = document.querySelector('#codigo');
const senha = document.querySelector('#senha');
const btnAlterar = document.querySelector('#btnAlterar');
const erroMsg2 = document.querySelector('#erroMsg2');
const overlay = document.querySelector('#overlay');

//funcoes

function validarSenha(senhaParaValidar) {
	if (senhaParaValidar.length < 8) {
		return false;
	} else {
		return true;
	}
}

//mascara cpf
$(document).ready(function() {
	$('#cpf').mask('000.000.000-00');
});


function verificarCampos(camposForm) {

	var camposPreenchidos = true;

	camposForm.forEach(function(campo) {
		if (!campo.value.trim()) {
			camposPreenchidos = false;
		}
	});

	if (!camposPreenchidos) {
		return false;
	} else {
		return true;
	}
}

// eventos


btnProsseguir.addEventListener('click', () => {

	event.preventDefault();

	const camposForm = [cpf, email];

	var validar = verificarCampos(camposForm);

	if (validar) {
		const dadosJuntos = {
			cpf: cpf.value,
			email: email.value
		};

		overlay.style.display = 'flex'

		fetch('/recuperarSenha/enviar', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(dadosJuntos)
		})

			.then(response => {
				//esconder o spinner 
				overlay.style.display = 'none';

				if (!response.ok) {
					return response.text()
						.then(erro => {
							erroMsg.textContent = erro;
							erroMsg.style.display = 'block';
						})
				} else {
					return response.text().then(msg => {
						form_principal.style.display = 'none';
						form_secundario.style.display = 'flex';
					})
				}
			}).catch(error => {
				//esconder o spinner 
				overlay.style.display = 'none';
				erroMsg.textContent = "Erro ao enviar os dados, tente novarmente.";
			})

	} else {
		erroMsg.textContent = 'Preencha todos os campos !';
		erroMsg.style.display = 'block';
	}

})

btnAlterar.addEventListener('click', () => {
	event.preventDefault();

	camposSub_Form = [codigo, senha];


	if (verificarCampos(camposSub_Form)) {
		const credenciais = {
			codigo: codigo.value,
			senha: senha.value
		};

		fetch('/recuperarSenha/recuperar', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(credenciais)
		})

			.then(response => {
				if (!response.ok) {
					return response.text().then(erro => {
						erroMsg2.textContent = erro;
						erroMsg2.style.display = 'block';
					})
				} else {
					return response.text().then(msg => {
						alert(msg);
						window.location.href = '/login';
					})
				}
			}).catch(error => {
				erroMsg2.textContent = 'Erro ao alterar, tente novamente.';
				erroMsg2.style.display = 'block';

			})
	} else {
		erroMsg2.textContent = 'Preencha todos os campos !';
		erroMsg2.style.display = 'block';
	}



})
