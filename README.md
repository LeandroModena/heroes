# App Heroes

## Visão Geral

Este aplicativo apresenta uma extensa lista de personagens de heróis, com a capacidade de marcar favoritos, ver detalhes ampliados e acessar informações offline. 
Inicialmente conectado a Api da Marvel porem extensível para outras Api (DC, Naruto, DB) devido a sua modularização.
O projeto segue o padrão de arquitetura MVVM, assegurando um código organizado.

## Funcionalidades

- **Exploração de Personagens**: Uma lista rolável dos personagens da Marvel.
- **Favoritos**: Capacidade de favoritar personagens na lista e na visualização de detalhes.
- **Acesso Offline**: Os personagens favoritados são salvos no dispositivo para acesso offline.
- **Pesquisa**: Barra de pesquisa para filtrar personagens por nome.
- **Visualização de Detalhes**: Página detalhada para cada personagem com imagem maior e descrição.
- **Tratamento de Erros**: Interfaces para listas vazias, erros e cenários de falta de internet.

## API

O aplicativo usa o endpoint "Characters" da API da Marvel para obter dados dos personagens. Mais informações podem ser encontradas na [documentação oficial da Marvel](https://developer.marvel.com/docs).

## Interface do Usuário

A interface é dividida em três seções principais:

1. **Pesquisa - Personagens**: Lista de personagens com opções para favoritar e uma barra de pesquisa.
2. **Detalhes do Personagem**: Visualização detalhada com opções de favorito e compartilhamento, imagem em tamanho ampliado e descrição.
3. **Favoritos**: Lista dos personagens favoritados pelo usuário com tratamento de erros correspondente.


## Execução do Projeto

1. Clone o repositório para sua máquina local.
2. Abra o projeto no Android Studio ou na IDE de sua preferência.
3. Crie sua api key em https://developer.marvel.com/account
4. Salve sua api privada com o nome MARVEL_API_KEY em local.properties
5. Salve sua api publica com o nome MARVEL_PUB_API_KEY em local.properties
6. Sincronize o projeto com os arquivos do Gradle.
7. Execute a aplicação em um emulador ou dispositivo físico.

## Documentação e Decisões de Projeto

Detalhes sobre métodos e suposições estão documentados no código. Em caso de requisitos ambíguos, as decisões tomadas são justificadas no README.

