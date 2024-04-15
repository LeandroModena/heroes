# App Heroes

## Visão Geral

Este aplicativo apresenta uma extensa lista de personagens de heróis, com a capacidade de marcar favoritos, ver detalhes ampliados e acessar informações offline. 
Inicialmente conectado a Api da Marvel porem extensível para outras Api (DC, Naruto, DB) devido a sua modularização.
O projeto segue o padrão de arquitetura MVVM e Compose, assegurando um código organizado e reutilizáveis.

## Funcionalidades

- **Exploração de Personagens**: Uma lista rolável dos personagens da Marvel paginada.
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
2. **Detalhes do Personagem**: Visualização detalhada com opções de compartilhamento da imagem.
3. **Favoritos**: Lista dos personagens favoritados e botão para remover favoritos.


## Execução do Projeto

1. Clone o repositório para sua máquina local.
2. Abra o projeto no Android Studio ou na IDE de sua preferência.
3. Crie sua api key em https://developer.marvel.com/account
4. Salve sua api privada com o nome MARVEL_API_KEY em local.properties.
5. Salve sua api publica com o nome MARVEL_PUB_API_KEY em local.properties.
6. Sincronize o projeto com os arquivos do Gradle.
7. Execute a aplicação em um emulador ou dispositivo físico.

## Documentação e Decisões de Projeto

Este aplicativo foi construido com:

- **[Jetpack Compose](https://developer.android.com/develop/ui/compose)** para criação de layout.
- **[Room](https://developer.android.com/training/data-storage/room)** para salvar dados.
- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** para injeção de dependência.
- **[Retrofit](https://square.github.io/retrofit/)** para requisições http.
- **[Coroutines](https://developer.android.com/kotlin/coroutines)** para serviços assíncrono.
- **[Flow](https://developer.android.com/kotlin/flow)** e **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)** para controle de estado.

Algumas considerações:

Para realizar uma request com flow no repositório utilize a função `requestByFlow { }` ela irá retornar um flow com a [classe `Result` do kotlin](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/)
Toda atividade desse projeto herda `BaseActivity` para inicialização.



