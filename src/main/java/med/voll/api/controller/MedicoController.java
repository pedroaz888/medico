package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;


    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        repository.save(new Medico(dados));

    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size=10, page=0, sort ={"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }



    @GetMapping("/busca")
    @Transactional
    public ResponseEntity<List <Medico>> buscarPeloNome(@RequestParam (name = "nome") String nome){

        List<Medico> medico = repository.findByNome(nome.toUpperCase());

        if(!medico.equals(nome)){
            return new ResponseEntity<List<Medico>>(medico, HttpStatus.NOT_FOUND);
        }else {

            return new ResponseEntity<List<Medico>> (medico, HttpStatus.OK);

        }
    }


//    List<ParkingSpotModel> parkingSpotModel = parkingSpotRepository.buscarPorProprietario(name.toUpperCase());
//
//        if(!parkingSpotModel.equals(name)) {
//
//        return new ResponseEntity<List<ParkingSpotModel>>(parkingSpotModel, HttpStatus.NOT_FOUND);
//    }else {
//
//        return new ResponseEntity<List<ParkingSpotModel>>(parkingSpotModel, HttpStatus.OK);
//    }
//}






    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico  = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    //EXCLUSÃO TOTAL
//    @DeleteMapping("/{id}")
//    public void deletar (@PathVariable (value ="id") Long id){
//        repository.deleteById(id);
//
//    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletar (@PathVariable (value ="id") Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();

    }

    @GetMapping ("/{id}")
    @Transactional
    public void ativar (@PathVariable (value ="id") Long id){
        var medico = repository.getReferenceById(id);
        medico.ativar();

    }

}

