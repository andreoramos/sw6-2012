# Group meeting 14/03-2012 #

Meeting takes place at 14/03-2012 @ 9:00 AM - 12:00 PM [0.2.13].

Responsible: sw602f12

Referent: sw602f12

## Agenda ##
  * For every individual group
    * Where are we?
      * What did we do since last time?
        * _Issues_
        * _Demonstrations_
    * Where are we going?
      * What are we going to work on until next time?
  * How far are we, with the entire multiproject? (concluded by the responsible group)
    * When are we at the next major milestone?
  * System Description - All groups (5 min each) (previously called system definition)
    * Launcher gruppen: [link](https://docs.google.com/document/d/1EguRj7PKEHhCozi88aTxw8lX8zUJuNjQIEIwJQNZHpM/edit)
    * Admin gruppen: [link](https://docs.google.com/document/d/1wNUwB9-O3B1Noqp_ka7grwfxxujQTLHhgAFBT_dSZ6U/edit)
    * Timer gruppen:  [link](https://docs.google.com/presentation/d/1ioRfCQk3TiGeSxew0r9alpMvvx48lTJiFugxCrJ84e8/edit)
    * Server gruppen: [link](https://docs.google.com/document/d/1Yo4IDL_IuE9QmWqNFqh22oonWy0_uSnbKKl4kBQLJd8/edit)
    * digiPecs gruppen (gruppen hedder nu "Parrot"): [link](https://docs.google.com/document/d/1zw-K7a5cG5A8RJKzPHQEBPoSljPk8XbSZt_JOZEHy2k/edit)
  * Multi Project System Description (Magnus) (10 min) [link](https://docs.google.com/document/d/1RpKheUhb6CQBLiBTWbUDeD5eZXFwZ38gC3cluCc6E2I/edit)
  * Iteration Length (Rasmus A) (10 min) [link](http://code.google.com/p/sw6-2012/source/browse/random_group_stuff/Group_timer/M%C3%B8der/14-3-2012/Pr%C3%A6sentation/Sprint%20Length.pdf)
  * Project Coordination (Rasmus A) (30 min) [link](http://code.google.com/p/sw6-2012/source/browse/random_group_stuff/Group_timer/M%C3%B8der/14-3-2012/Pr%C3%A6sentation/Project%20Coordination.pdf)
  * Application Execution Environment (Rasmus DJ) (10 min)
  * GUI Meetings with PhD Student from IS Group Henrik Sørensen (Ulrik) (10 min)
  * Aub. (Eventuelt)
  * Next Meeting
    * Responsible group (sw603f12)
    * Date (sw603f12)
    * Open Door Day (Rasmus A)

Issues in _italic_ is optional

--- --- Summary: Summary of group meeting on the 14th of march, 2012.
Danish version (use google translate for english version)

Status:
  * DigiPecs:
Har været ved at tage kontakt til kontaktperson, og dem der arbejde på digiPecs sidste år.
Til næste møde vil de finde ud af om de vil/kan arbejde videre på sidste års kode.

  * Timer:
Har holdt første møde med kontakt, og fået noget godt input. Timeren er delt op i 3 dele, og der er begyndt på use cases til dem.
Til næste gang vil der være flere use cases.

  * Server:
De er ved at finde ud af hvordan data skal flyttes mellem launcher, admin og server, og er ved at finde ud af hvordan det generelt skal sættes op, f.eks. med en settings XML fil. Det vil fortsættes til næste gang.

  * Admin:
Er ved at finde ud af hvad de kan genbruge fra sidst. Er ved at kigge på info fra server gruppen vedr. settings XML. Ved at sætte database op, og næste gang vil de fremlægge noget om det.

  * Launcher:
Er ved at udfylde backlog. Vil i gang med at lave prototyper til deres kontakt, så de kan vise noget til deres kontakt og få noget feedback.

Forslag om at begynde sprint på mandag... ikke noget vedtaget endnu.

Magnus: Fælles del af rapport.
Lidt ændringer tilføjet: en samlet definition af platformen. Ulrik synes det er en god idé at have med. Gerne et lille bitte kort afsnit om processen for hvordan vi er kommet frem til den samlede system/platform beskrivelse. Det blev vedtaget.
**Til hver overskrift i den samlede del, skal der skrives 3-5 sætninger til hvad der skal stå i afsnittene. Ulrik mener at den første del gerne skal være færdig i løbet af en måned.** Det blev vedtaget at launcher gruppen tager det første afsnit (motivation), og så mødes vi mandag med kommentarer til det mandag, og så vælges næste gruppe til at tage næste afsnit.

Projektkoordinering
Rasmus Aaen fremlægger om sprints, og kommer med forslag til hvordan næste sprint skal foregå.
  * Det blev vedtaget at vi starter sprint mandag og slutter fredag.
  * Det blev vedtaget at alle backlogs skal være tilgængelige for alle, men man retter ikke i hindandens backlogs! Så snakker man med den pågældende gruppe.
  * Det blev også vedtaget at vi kører synkrone sprints.
  * Det blev vedtaget at vi kører self-controlled sprints, dvs. folk bestemmer selv hvad der skal ske i de forskellige sprints.

Rasmus DJ
Apps som stand-alone eller som datapakker i launcheren.
  * Vedtaget at vi udvikler til giraf-launcheren, (nedarver fra GirafActivity), og så kan man selv lave funktionalitet til at køre stand-alone, hvis ikke Giraf launcheren er installeret.

Ulrik
Forslag om at holde møde/workshop med en GUI PhD studerende fra IS. Vi kommer med vores status, hvor er vi med vores GUI og hvordan ser den ud, og hvad har vi tænkt med designet. Så kommer ekspernet med feedback og kommentarer. Det foregår som udgangspunkt på design-niveau, evt. med papir-prototyper.
  * Vedtaget at vi gerne vil have ekspert-hjælp. Thomas Panum står for første kontakt og mødeindkaldelse.

Eventuelt:
  * Ulrik - tid til individuelle grupper. Det bliver torsdag d. 15. marts mellem 12:30 og 14:00. Det bliver bare nogle korte 15 minutters møder, lige for at komme i gang.
  * Thomas Panum - Snakker GUI. Han har idé om at outsource udvikling af logoer til apps, så vi skriver bare forslag til Thomas, og så udvikler de muligvis logoerne til os.
  * Ulrik - Rapport på engelsk og apps på dansk. Måske skulle vi tage en bevidst beslutning om at lave settings XML på både dansk og engelsk, så apps kan gøre på både dansk og engelsk (evt flere sprog). Det blev vedtaget at vi laver apps på dansk, og så bruger vi resource filen, som er god android skik, og så vil det være nemt at skfite sprog senere, hvis det skulle blive nødvendigt.
  * Rasmus Aaen - Det blev vedtaget at vi tilføjer et punkt på agendaen til, så ansvarsrollerne kan komme med status.
  * Pædagogerne vil gerne have at vi siger “børn med autismespektrumforstyrrelse” og ikke “autister”.