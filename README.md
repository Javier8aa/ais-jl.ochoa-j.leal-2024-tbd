# AIS-Practicas-4y5-2024

Autor(es): Jorge Leal & Javier Laureano Ochoa

[Repositorio](https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd)

[Aplicación Azure](http://nitflex-jljlo.westeurope.azurecontainer.io:8080/)

Antes de comenzar la practica 5, y con ello el fix y la funcionalidad, hicimos varias pruebas para verificar el correcto funcionamiento de los workflows, desencadenando así algún pull-request, push...

Por lo que la práctica 5 comienza tras el commit "Readme actualizado previo P5" y la etiqueta de git

## Desarrollo con GitHubFlow (Práctica 5)

Una vez creados los workflows y funcionando estos, pasamos a crear la nueva funcionalidad utilizando GithubFlow:

1. Clonamos el reporsitorio
```
$ git clone https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd.git
```

2. Creamos rama del fix (Javier)
```
$ git checkout -b fix/cancel-button-bug
```

3. Añadimos arreglos bug y test del fix (Javier)
```
$ git add .
```

4. Realizamos el commit del fix (Javier)
```
$ git commit -m "Fix cancel button bug and add regression test"
```

5. Realizamos el push del fix (Javier)
```
git push origin fix-cancel-edition-1
```

Ejecucion Workflow1: [Workflow 1](https://github.com/Javier8aa/ais-jl.ochoa-j.leal-2024-tbd/actions/runs/9749709406)