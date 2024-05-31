package com.example.gticslab720212607.controller;

import com.example.gticslab720212607.entity.Resources;
import com.example.gticslab720212607.entity.Users;
import com.example.gticslab720212607.repository.ResourcesRepository;
import com.example.gticslab720212607.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class AuthenticationController {

    @Autowired
    ResourcesRepository resourcesRepository;
    @Autowired
    UsersRepository usersRepository;


    // Agregar Usuario Contador
    @PostMapping(value = { "/agregarUsuario"})
    public ResponseEntity<HashMap<String, Object>> guardarUsuario(
            @RequestBody Users users) {

        HashMap<String, Object> responseJson = new HashMap<>();


        switch (users.getType()){
            case "Contador":
                users.setAuthorizedResource(resourcesRepository.findById(5).get());
            case "Cliente":
                users.setAuthorizedResource(resourcesRepository.findById(6).get());
            case "AnalistaPromociones":
                users.setAuthorizedResource(resourcesRepository.findById(7).get());
            case "AnalistaLogistico":
                users.setAuthorizedResource(resourcesRepository.findById(8).get());
            default:
                users.setAuthorizedResource(resourcesRepository.findById(5).get());
        }

        usersRepository.save(users);
        responseJson.put("estado", "creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }



    //LISTAR por recurso
    @GetMapping(value = {"/list/{recursoId}"})
    public ResponseEntity<HashMap<String, Object>> listaUsuariosPorRecurso(@PathVariable("recursoId") String recursoId) {

        try {
            int id = Integer.parseInt(recursoId);
            Optional<Resources> byId = resourcesRepository.findById(id);

            HashMap<String, Object> respuesta = new HashMap<>();
            if (byId.isPresent()) {
                List<Users> usersList = usersRepository.findByAuthorizedResource(byId.get());
                respuesta.put("result", "ok");
                respuesta.put("lista", usersList);
            } else {
                respuesta.put("result", "el recurso no existe");
            }
            return ResponseEntity.ok(respuesta);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HashMap<String, Object>> borrar(@RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);

            HashMap<String, Object> rpta = new HashMap<>();

            Optional<Users> byId = usersRepository.findById(id);
            if(byId.isPresent()){
                usersRepository.deleteById(id);
                rpta.put("result","ok");
                rpta.put("msg","se eliminó correctamente");
            }else{
                rpta.put("result","no ok");
                rpta.put("msg","el ID enviado no existe");
            }

            return ResponseEntity.ok(rpta);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ACTUALIZAR
    @PutMapping(value = { "/actualizar"})
    public ResponseEntity<HashMap<String, Object>> actualizar(Users userRecibido) {

        HashMap<String, Object> rpta = new HashMap<>();

        if (userRecibido.getId() != null && userRecibido.getId() > 0) {

            Optional<Users> byId = usersRepository.findById(userRecibido.getId());
            if (byId.isPresent()) {
                Users userDb = byId.get();

                if (userRecibido.getName() != null)
                    userDb.setName(userRecibido.getName());

                if (userRecibido.getActive() != null)
                    userDb.setActive(userRecibido.getActive());

                if (userRecibido.getAuthorizedResource() != null)
                    userDb.setAuthorizedResource(userRecibido.getAuthorizedResource());

                if (userRecibido.getType() != null){
                    userDb.setType(userRecibido.getType());
                    switch (userRecibido.getType()){
                        case "Contador":
                            userDb.setAuthorizedResource(resourcesRepository.findById(5).get());
                        case "Cliente":
                            userDb.setAuthorizedResource(resourcesRepository.findById(6).get());
                        case "AnalistaPromociones":
                            userDb.setAuthorizedResource(resourcesRepository.findById(7).get());
                        case "AnalistaLogistico":
                            userDb.setAuthorizedResource(resourcesRepository.findById(8).get());
                        default:
                            userDb.setAuthorizedResource(resourcesRepository.findById(5).get());
                    }
                }
                usersRepository.save(userDb);

                rpta.put("result", "ok");
                return ResponseEntity.ok(rpta);
            } else {
                rpta.put("result", "error");
                rpta.put("msg", "El ID del usuario enviado no existe");
                return ResponseEntity.badRequest().body(rpta);
            }
        } else {
            rpta.put("result", "error");
            rpta.put("msg", "debe enviar un usuario con ID");
            return ResponseEntity.badRequest().body(rpta);
        }
    }





    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> gestionException(HttpServletRequest request) {
        HashMap<String, String> responseMap = new HashMap<>();
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Error detectado");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }

}
