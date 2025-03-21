package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// tag::hateoas-imports[]
// end::hateoas-imports[]

@RestController
class ClienteController {

	private final ClienteRepository repository;

	ClienteController(ClienteRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	// tag::get-aggregate-root[]
	@GetMapping("/clientes")
	@CrossOrigin(origins = "http://localhost:4200")
	List<EntityModel<Cliente>> all() {

		List<EntityModel<Cliente>> clientes = repository.findAll().stream()
				.map(cliente -> EntityModel.of(cliente))
				.collect(Collectors.toList());

		return clientes;
	}
	// end::get-aggregate-root[]

	@PostMapping("/clientes")
	@CrossOrigin(origins = "http://localhost:4200")
	Cliente newCliente(@RequestBody Cliente newCliente) {
		return repository.save(newCliente);
	}

	// Single item

	// tag::get-single-item[]
	@GetMapping("/clientes/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	EntityModel<Cliente> one(@PathVariable Long id) {

		Cliente cliente = repository.findById(id) //
				.orElseThrow(() -> new ClienteNotFoundException(id));

		return EntityModel.of(cliente);
	}
	// end::get-single-item[]

	@PutMapping("/clientes/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	Cliente replaceCliente(@RequestBody Cliente newCliente, @PathVariable Long id) {

		return repository.findById(id) //
				.map(cliente -> {
					cliente.setNome(newCliente.getNome());
					cliente.setCarroModelo(newCliente.getCarroModelo());
					cliente.setAno(newCliente.getAno());
					cliente.setData(newCliente.getData());
					cliente.setServico(newCliente.getServico());
					cliente.setValor(newCliente.getValor());
					cliente.setEmail(newCliente.getEmail());
					cliente.setFirebaseId(newCliente.getFirebaseId());
					return repository.save(cliente);
				}) //
				.orElseGet(() -> {
					return repository.save(newCliente);
				});
	}

	@DeleteMapping("/clientes/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	void deleteCliente(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
