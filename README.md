# Documentatia pentru dezvoltatorii de programe software care doresc sa integreze prin API sistemul REGES-ONLINE

## 1. Introducere

Sistemul REGES-Online va fi compus din 2 aplicatii expuse catre public:
1. Portalul cetateanului disponibil la adresa reges.inspectiamuncii.ro
2. Aplicatia angajatorului, disponibila la adresa reges.inspectiamuncii.ro
3. API de acces la aplicatia angajatorului disponibila la adresa api.inspectiamuncii.ro

Accesul la primele 2 aplicatii se va face folosind mai multe mecanisme de acces precum
- user/parola
- identitate digitala ROeID sau eIDAS
- certificat digital calificat

Accesul la API se va face folosind un token de acces individualizat pe CUI/CIF care se va obtine din aplicatia web a angajatorului.

Pentru o buna intelegere a documentatiei tehnice recomandam familiarizarea cu aplicatia Revisal disponibila pentru download pe situl www.inspectiamuncii.ro [Revisal 6.0.9](https://www.inspectiamuncii.ro/reges)

## 2. Principii de functionare API

API expune catre terti puncte de acces pentru:
1. Transmiterea de informatii 
2. Obtinerea raspunsurilor

Protocolul de acces API este HTTP iar formatarea mesajelor transmise este XML sau JSON. 

Veti regasi exemple de mesaje in fisierul [POSTMAN](RevisalEvents.postman_collection.json) atat pentru formatul XML cat si JSON.

Datele transmise prin API se vor formata respectand schema [Schema REGES 2025](<Schema reges.xsd>).

Toate informatiile transmise catre REGES-Online se ordoneaza intr-o coada si se proceseaza in ordinea FIFO (First In First Out).

Metodele folosite pentru transmiterea de informatii si verificarea raspunsurilor sunt HTTP POST.

### 2.1 Transmiterea de informatii

Se vor putea transmite urmatoarele informatii catre REGES-Online:
1. Adaugari/Modificari ale salariatilor
2. Adaugari/Modificari ale contractelor
3. Actiuni asociate contractelor existente (suspendari, detasari, etc.)

### 2.2 Obtinerea raspunsurilor

Raspunsurile de la API sunt de 2 tipuri:
1. Raspunsuri sincrone, ca urmare a unui apel de metoda API este returnat un mesaj de tip MessageResponse, care contine un ID de raspuns, denumit si recipisa, acest ID confirma inregistrarea solicitarii si depozitarea acesteia in coada de procesare
2. Raspunsuri asincrone, ca urmare a procesarii REGES-Online a unui apel API, dupa ce s-a finalizat respectiva operatione se pune in coada API a angajatorului respectiv un mesaj de tip MessageResult, mesaj care contine un identificator al rezultatului (cod contract, cod salariat) precum si un cod de succes/fail impreuna cu o explicatie a problemei intalnite.

## 3. Structura datelor si a mesajelor

Schema [Schema Reges 2025](<Schema reges.xsd>) contine toate timpurile de date care pot fi vehiculate de API. 

Un element de baza este tipul Message din care se extind alte tipuri de mesaje. Tipul Message contine obligatoriu o structura de date numita Header in care cel care trimite informatii prin API se identifica, anunta operatia dorita precum si alte metadate utile.

![Structura de date tip Header din tipul Message](images/header.JPG)

API accepta urmatoarele tipuri de mesaje prin interfata publica (api.inspectiamuncii.ro)

1. Contract
2. Salariat

Mesajele au asociat un tip MessageType, cele de interes pentru API fiind cele specifice contractului (AdaugareContract, RadiereContract, etc.) precum si cele specifice salariatului (InregistrareSalariat, ModificareSalariat, etc.)

## 4. Exemple de mesaje

### 4.1 Modificare contract

Urmatorul mesaj este un exemplu de modificare de contract pentru situatia in care s-a incheiat un acti aditional care actualizeaza elemente din contract care se raporteaza in registrul REGES

    <?xml version="1.0" encoding="UTF-8"?>
    <Message xsi:type="Contract" xmlns="http://www.inspectiamuncii.ro/reges2025"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.inspectiamuncii.ro/reges2025">
        <Header>
            <MessageId>117f9b03-9efb-4f5e-8eab-7ab3b0c792af</MessageId>
            <ClientApplication>117f9b03-9efb-4f5e-8ebb-7ab3b0c792bf</ClientApplication>
            <Version>5</Version>
            <Operation>ModificareContract</Operation>
            <AuthorId>117f9b03-9efb-4f5e-8ebb-7ab3b0c792cf</AuthorId>
            <SessionId>117f9b04-9efb-4f5e-8ebb-7ab3b0c792cf</SessionId>
            <User>Ion</User>
            <Timestamp>2024-06-18T14:19:58.917Z</Timestamp>
        </Header>
        <Continut>
            <ReferintaSalariat>
                <Id>117f9b03-9efb-4f5e-8eaa-7ab3b0c792cf</Id>
            </ReferintaSalariat>
            <Cor>
                <Cod>12345</Cod>
                <Versiune>2</Versiune>
            </Cor>
            <DataConsemnare>2024-06-18T14:19:58.917Z</DataConsemnare>
            <DataContract>2024-06-18T14:19:58.917Z</DataContract>
            <DataInceputContract>2024-06-18T14:19:58.917Z</DataInceputContract>
            <NumarContract>12345</NumarContract>
            <Radiat>false</Radiat>
            <Salariu>1200</Salariu>
            <StareCurenta>
            </StareCurenta>
            <TimpMunca>
                <Norma>NormaIntreaga840</Norma>
                <Repartizare>OreDeZi</Repartizare>
            </TimpMunca>
            <TipContract>ContractIndividualMunca</TipContract>
            <TipDurata>Nedeterminata</TipDurata>
            <TipNorma>NormaIntreaga</TipNorma>
        </Continut>
    </Message>

Observam faptul ca **salariatul** este transmis ca referinta, el ne fiind actualizat cu ocazia acestui mesaj.

### 4.2 Modificare contract cu actiune de incetare

    <?xml version="1.0" encoding="UTF-8"?>
    <Message xsi:type="Contract" xmlns="http://www.inspectiamuncii.ro/reges2025"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.inspectiamuncii.ro/reges2025">
        <Header>
            <MessageId>117f9b03-9efb-4f5e-8eab-7ab3b0c792af</MessageId>
            <ClientApplication>117f9b03-9efb-4f5e-8ebb-7ab3b0c792bf</ClientApplication>
            <Version>5</Version>
            <Operation>ModificareContract</Operation>
            <AuthorId>117f9b03-9efb-4f5e-8ebb-7ab3b0c792cf</AuthorId>
            <SessionId>117f9b04-9efb-4f5e-8ebb-7ab3b0c792cf</SessionId>
            <User>Ion</User>
            <Timestamp>2024-06-18T14:19:58.917Z</Timestamp>
        </Header>
        <ReferintaContract>
            <Id>726ca2db-032e-4ea7-be31-f8d8489796f4</Id>
        </ReferintaContract>
        <Actiune xsi:type="ActiuneIncetare">
            <DataIncetare>2024-06-25T12:43:04.326Z</DataIncetare>
            <Explicatie>Indisciplina</Explicatie>
            <TemeiLegal>Art55LitB</TemeiLegal>
        </Actiune>
    </Message>

Observam cum **contractul** este transmis ca referinta, continutul acestuia ne fiind actualizat cu ocazia transmiterii acestui mesaj.

Referintele la contracte si salariati le veti putea obtine din mesajul MessageResult, in urma procesarii cu succes a acestora.

### 4.3 Inregistrare salariat

Mesajul urmator inregistreaza un salariat nou in sistem.

    <?xml version="1.0" encoding="UTF-8"?>
    <Message xsi:type="Salariat" xmlns="http://www.inspectiamuncii.ro/reges2025"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.inspectiamuncii.ro/reges2025">
        <Header>
            <MessageId>117f9b03-9efb-4f5e-8ebb-7ab3b0c792af</MessageId>
            <ClientApplication>117f9b03-9efb-4f5e-8ebb-7ab3b0c792bf</ClientApplication>
            <Version>5</Version>
            <Operation>InregistrareSalariat</Operation>
            <AuthorId>117f9b03-9efb-4f5e-8ebb-7ab3b0c792cf</AuthorId>
            <SessionId>117f9b04-9efb-4f5e-8ebb-7ab3b0c792cf</SessionId>
            <User>Ion</User>
            <Timestamp>2024-06-18T14:19:58.917Z</Timestamp>
        </Header>
            <Adresa>STR. SALARIATULUI, NR. 1</Adresa>
            <Cnp>1800612015459</Cnp>
            <Nume>POPESCU</Nume>
            <Prenume>ION</Prenume>
            <Nationalitate>
                <Nume>ROMÂNIA</Nume>
            </Nationalitate>
            <TaraDomiciliu>
                <Nume>ROMÂNIA</Nume>
            </TaraDomiciliu>
            <TipActIdentitate>CarteIdentitate</TipActIdentitate>
    </Message>

## 5 Nomenclatoare

Majoritatea tipurilor de date din schema XSD contin si nomenclatoarele incluse. Nomenclatoarele COR, CAEN si sporuri vor fi expuse de API in format JSON si XML.

Puteti consulta aici o varianta draft a acestora. [Nomenclatoare in format JSON](nomenclatoare/nomenclatoare.json)