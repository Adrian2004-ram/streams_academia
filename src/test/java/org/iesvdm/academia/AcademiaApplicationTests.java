package org.iesvdm.academia;

import org.iesvdm.academia.modelo.Alumno;
import org.iesvdm.academia.modelo.Curso;
import org.iesvdm.academia.modelo.Matricula;
import org.iesvdm.academia.modelo.MatriculaId;
import org.iesvdm.academia.repositorio.AlumnoRepository;
import org.iesvdm.academia.repositorio.CursoRepository;
import org.iesvdm.academia.repositorio.MatriculaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Locale.filter;

@SpringBootTest
class AcademiaApplicationTests {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void testAlumnos() {

        alumnoRepository.findAll().forEach(System.out::println);

    }

    @Test
    void testCursos() {

        cursoRepository.findAll().forEach(System.out::println);

    }

    @Test
    void testMatriculas() {

        matriculaRepository.findAll().forEach(System.out::println);

    }

    /**
     *  1. Devuelve un listado de todos los cursos que se realizaron con fecha de inicio y fin durante el año 2025,
     *   cuya precio base sea superior a 300€.
     *
     */
    @Test
    void test1() {
        List<Curso> cursos = cursoRepository.findAll();
/*
        List<Curso> cur2025 = cursos.stream()
                .map(cur -> cur)
                .filter(cur -> ((int) cur.getFin().getYear() == null ? false : cur.getFin().getYear() == 2025) &&
                                (cur.getFin().getYear() == null ? false : cur.getFin().getYear() == 2025) &&
                                (cur.getPrecioBase() > 300))
                .toList();

        cursos.forEach(System.out::println);
*/

    }

    /**
     * 2. Devuelve un listado de todos los alumnos que NO se han matriculado en ningún curso.
     */
    @Test
    void test2() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        List<Alumno> alumNoMatric = alumnos.stream()
                .filter(alum -> alum.getMatriculas().isEmpty())
                .toList();

        alumNoMatric.forEach(System.out::println);

        Assertions.assertEquals(1, alumNoMatric.size());

    }

    /**
     *  3. Devuelve una lista de los id's, nombres y emails de los alumnos que no tienen el teléfono registrado.
     *  El listado tiene que estar ordenado inverso alfabéticamente por nombre (z..a)
     */
    @Test
    void test3() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        var  alumNoTelf = alumnos.stream()
                .filter(alumn -> (alumn.getTelefono() == null))
                .sorted(Comparator.comparing(Alumno::getNombre).reversed())
                .map(alumn -> "id: " + alumn.getId() + ", nombre: " + alumn.getNombre() + ", email: " + alumn.getEmail() + ", telf: " + alumn.getTelefono())
                .toList();

        alumNoTelf.forEach(System.out::println);

        Assertions.assertEquals(26, alumNoTelf.size());

    }

    /**
     * 4. Devuelva un listado con los id's y emails de los alumnos que se hayan registrado con una cuenta de yahoo.es
     * en el año 2024.
     *
     */
    @Test
    void test4() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        var  alumYahoo = alumnos.stream()
                .filter(alum -> alum.getEmail().contains("yahoo.es"))
                .map(alum -> "id: " + alum.getId() + ", email: " + alum.getEmail())
                .toList();

        alumYahoo.forEach(System.out::println);

        Assertions.assertEquals(11, alumYahoo.size());

    }

    /**
     * 5. Devuelva un listado de los alumnos cuyo primer apellido es Martín. El listado tiene que estar ordenado
     * por fecha de alta en la academia de más reciente a menos reciente y nombre y apellidos en orden alfabético.
     */
    @Test
    void test5() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        List<Alumno>  alumMartin = alumnos.stream()
                .filter(alum -> alum.getNombre().contains(" Martín "))
                .sorted(Comparator.comparing(Alumno::getFechaAlta).reversed().thenComparing(Alumno::getNombre))
                .toList();

        alumMartin.forEach(System.out::println);

        Assertions.assertEquals(5, alumMartin.size());

    }

    /**
     * 6. Devuelva gasto total (pagado) que ha realizado la alumna Claudia López Rodríguez en cursos en la academia.
     *
     */
    @Test
    void test6() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        var gastoTotal = alumnos.stream()
                .filter(alum -> alum.getNombre().equals("Claudia López Rodríguez"))
                .map(clau -> clau.getMatriculas())
                .map(matri -> matri.stream()
                                                            .mapToDouble(x -> x.getPrecioPagado())
                                                            .sum()
                ).findFirst();

        System.out.println(gastoTotal);

        Assertions.assertEquals(1270.63, gastoTotal.get());

    }

    /**
     * 7. Devuelva el listado de los 3 cursos de menor importe base.
     *
     */
    @Test
    void test7() {
        List<Curso> cursos = cursoRepository.findAll();

        List<Curso> curMenImpo = cursos.stream()
                .sorted(Comparator.comparing(Curso::getPrecioBase))
                .limit(3)
                .toList();

        curMenImpo.forEach(System.out::println);

        Assertions.assertEquals(3, curMenImpo.size());

    }

    /**
     * 8. Devuelva el curso al que se le ha aplicado el mayor descuento en cuantía para una matrícula
     * (no tiene por qué ser de descuento porcentual) sobre su precio base.
     *
     */
    @Test
    void test8() {
        List<Curso> cursos = cursoRepository.findAll();

        record CursoMatri(Long idCurso, double cantDescontada) {}

        CursoMatri curMayorDesc = cursos.stream()
                .flatMap(curs ->
                        curs.getMatriculas().stream()
                                .map(mat -> new CursoMatri(curs.getId(),
                                        (curs.getPrecioBase() - mat.getPrecioPagado()))
                        )
                ).sorted(Comparator.comparing(CursoMatri::cantDescontada).reversed())
                .findFirst().orElse(new CursoMatri(null, 0));


        System.out.println(curMayorDesc.idCurso() + " - " +  curMayorDesc.cantDescontada());

        Assertions.assertEquals(4, curMayorDesc.idCurso());

    }

    /**
     *
     * 9. Devuelve los alumnos que hayan obtenido un 10 como nota final en algún curso del que se han matriculado.
     *
     *
     */
     @Test
     void test9() {
         List<Alumno> alumnos = alumnoRepository.findAll();

         record alumMatri(Long idAlumno, String nombre,  MatriculaId idMatricula , double notaFinal) {}

         var alumNotaFinal = alumnos.stream()
                        .flatMap(alum ->
                                alum.getMatriculas().stream()
                                     .map(mat -> new alumMatri(alum.getId(),
                                             alum.getNombre(),
                                             mat.getId(),
                                             mat.getNotaFinal() == null? 0 : mat.getNotaFinal()
                                     )
                        )
                ).filter(ma -> ma.notaFinal == 10)
                                .toList();

         alumNotaFinal.forEach(System.out::println);

         Assertions.assertEquals(2, alumNotaFinal.size());

     }

    /**
     * 10. Devuelva el valor de la mínima nota obtenida en un curso.
     */
    @Test
    void test10() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        record alumMatri(Long idAlumno, String nombre,  MatriculaId idMatricula , double notaFinal) {}

        var alumMinimaNota = matriculas.stream()
                .filter(matri -> !(matri.getNotaFinal() == null))
                .map(x -> x.getNotaFinal())
                .sorted()
                .findFirst().orElse(0.0);

        System.out.println("La minima nota sacada es: " + alumMinimaNota);

        Assertions.assertEquals(5.19, alumMinimaNota);

    }

    /**
     *  11. Devuelve un listado de los cursos que empiecen por A y terminen por t,
     *  y también los cursos que terminen por x.
     */
    @Test
    void test11() {
        List<Curso> cursos = cursoRepository.findAll();

        List<Curso> listCurs = cursos.stream()
                .filter(cur -> (cur.getTitulo().startsWith("A") &&
                        cur.getTitulo().endsWith("t")) ||
                        cur.getTitulo().endsWith("x")
                ).toList();

        listCurs.forEach(System.out::println);

        Assertions.assertEquals(2, listCurs.size());


    }

    /**
     * 12. Devuelve un listado que muestre todos los cursos en los que se ha matriculado cada alumno.
     * El resultado debe mostrar todos los datos del alumno primero junto con un sublistado de sus cursos.
     * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre.
     */
    @Test
    void test12() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        var currMatr = alumnos.stream()
                .sorted(Comparator.comparing(Alumno::getNombre))
                .map(alumno -> {
                    String datosAlumno = "Id: " +  alumno.getId() +
                            "\n Nombre: " + alumno.getNombre() +
                            "\n Email: " + alumno.getEmail() +
                            "\n Telefono: " + alumno.getTelefono() +
                            "\n FechaAlta: " + alumno.getFechaAlta() +
                            "\n Cursos Matriculados: \n";

                    String datosMatri = alumno.getMatriculas().stream()
                                        .map(mat -> mat.getCurso().getTitulo())
                            .collect(Collectors.joining(" \n    "));

                    String result = datosAlumno + "    " + datosMatri;

                    return result;
                }).toList();

        currMatr.forEach(System.out::println);

        Assertions.assertEquals(81, currMatr.size());

    }

    /**
     * 13. Devuelve el total de alumnos que podrían matricularse en la academia en base al cupo de los cursos.
     */
    @Test
    void test13() {
        List<Curso> curso = cursoRepository.findAll();

        var totalAlum = curso.stream()
                .mapToInt(cur -> cur.getCupo()  == null? 0 : cur.getCupo() - cur.getMatriculas().stream().toList().size())
                .sum();

        System.out.println("Total alumnos que podrian unirse: " + totalAlum);

        Assertions.assertEquals(53, totalAlum);

    }

    /**
     * 14. Calcula el número total de alumnos (diferentes) que tienen alguna matrícula.
     */
    @Test
    void test14() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        var alumMatriculado = matriculas.stream()
                .map(x -> x.getAlumno().getId())
                .distinct()
                .count();

        System.out.println("Total alde alumnos que tienen matriculas: " + alumMatriculado);

        Assertions.assertEquals(80, alumMatriculado);

    }


    /**
     * 15. Devuelve el listado de cursos a los que se aplica un descuento porcentual (descuento_pct) superior al 10%
     */
    @Test
    void test15() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        List<Curso> cursosConDesc = matriculas.stream()
                .filter(mat -> mat.getDescuentoPct() == null? false : mat.getDescuentoPct()>10.0)
                .map(mat -> mat.getCurso())
                .distinct()
                .toList();

        cursosConDesc.forEach(System.out::println);

        Assertions.assertEquals(10, cursosConDesc.size());


    }


    /**
     * 16. Devuelve el nombre del alumno que pago la matrícula por curso de mayor cuantía.
     */
    @Test
    void test16() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        var alumMasPago = matriculas.stream()
                .sorted(Comparator.comparing(Matricula::getPrecioPagado))
                .map(mat -> mat.getAlumno().getNombre())
                .findFirst();

        System.out.println("El alumno que pago mayor cuantia por el curso es: " + alumMasPago.get());

        Assertions.assertEquals(alumMasPago.get(), "Noelia Muñoz Martínez");

    }

    /**
     * 17. Devuelve los nombre de los alumnos que hayan sido compañeros en algún curso de la alumna Claudia López Rodríguez
     */
    @Test
    void test17() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        var numAlum = matriculas.stream()
                .filter(mat -> mat.getAlumno().getNombre().equals("Claudia López Rodríguez"))
                .flatMap(mat -> mat.getCurso().getMatriculas().stream())
                .map(mat -> mat.getAlumno())
                .distinct()
                .map(alumno -> alumno.getNombre())
                .filter(alum -> !alum.equals("Claudia López Rodríguez"))
                .toList();

        numAlum.forEach(System.out::println);

        Assertions.assertEquals(42, numAlum.size());

    }

    /**
     *
     * 18. Devuelve el total de lo ingresado por la academia en matriculas para el mes de enero de 2025.
     */
    @Test
    void test18() {
        List<Matricula> matriculas = matriculaRepository.findAll();


    }

    /**
     *  19. Devuelve el conteo de cuantos alumnos tienen la observación de 'Requiere apoyo' en los cursos matriculados.
     */
    @Test
    void test19() {
        List<Matricula> matriculas = matriculaRepository.findAll();

        var alumCont = matriculas.stream()
                .filter(mat -> mat.getObservaciones() == null ? false : mat.getObservaciones().equals("Requiere apoyo"))
                .map(mat -> mat.getAlumno())
                .distinct()
                .count();

        System.out.println("Estos son los alumnos que tienen que recibir apollo en algún curso: " +  alumCont);

        Assertions.assertEquals(29,  alumCont);

    }

    /**
     *  20. Devuelve cuánto se ingresaría por el curso de 'Desarrollo Backend con Java' si todo el cupo
     *  de alumnos estuviera matriculdo.
     */
    @Test
    void test20() {

        List<Curso> curso = cursoRepository.findAll();

        int totalAlumn = alumnoRepository.findAll().size();

        var costeCurso = curso.stream()
                .filter(cur -> cur.getTitulo().equals("Desarrollo Backend con Java"))
                .mapToDouble(cur  -> cur.getPrecioBase())
                .findFirst();

        var ingresaria = totalAlumn * costeCurso.getAsDouble();

        System.out.println("Por el curso de Desarrollo Backend con Java se ingresaria: " + ingresaria);

        Assertions.assertEquals(42120.0, ingresaria);


    }


}
