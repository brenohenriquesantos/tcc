package dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ComentarioDTO {
	private Long idUsuario;
	private Long idEstabelecimento;
	private String texto;
}
