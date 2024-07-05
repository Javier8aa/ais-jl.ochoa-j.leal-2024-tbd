AIS-Practicas-4y5-2024

Autor(es): Jorge Leal & Javier Laureano Ochoa

[Repositorio](https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd)

[Aplicación Azure](http://nitflex-jljlo.westeurope.azurecontainer.io:8080/)

Antes de comenzar la practica 5, y con ello el fix y la funcionalidad, hicimos varias pruebas para verificar el correcto funcionamiento de los workflows, desencadenando así algún action invalido debido a fallos en azure con semver

Por lo que la práctica 5 comienza tras el commit "Readme actualizado previo P5" y la etiqueta de git

## Desarrollo con GitHubFlow (Práctica 5)

Una vez creados los workflows y funcionando estos, pasamos a crear la nueva funcionalidad utilizando GithubFlow:

Clonamos el repositorio
 
1. Clonamos el repositorio
```
$ git clone https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd.git
```

2. Creamos rama para la feature (Jorge)

git checkout -b  feature/validate-movie-year

3. Añadimos los cambios al stage y hacemos un commit de la feature (Jorge)

git commit -am "Añadida funcionalidad de comprobación de año no válido"


4. Hacemos push de la rama feature (Jorge)

git push origin feature/validate-movie-year
```
Ejecución Workflow1 [Workflow 1](https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd/actions/runs/9806245806)