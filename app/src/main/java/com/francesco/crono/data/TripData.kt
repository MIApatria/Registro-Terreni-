package com.francesco.crono.data

import com.francesco.crono.model.BoxKind
import com.francesco.crono.model.ChecklistItem
import com.francesco.crono.model.Day
import com.francesco.crono.model.Event
import com.francesco.crono.model.InfoBox
import com.francesco.crono.model.Segment

/**
 * Contenuto del "Road Trip USA 2026 - Cronoprogramma v2 (allineato ai biglietti)".
 * Tutti i dati sono statici: nessuna connessione di rete richiesta.
 */
object TripData {

    const val TRIP_TITLE = "Road Trip USA 2026"
    const val TRIP_SUBTITLE = "West Coast · California · Nevada · Arizona · Utah"
    const val TRIP_DATES = "Sab 3 → Gio 15 ottobre · 12 notti · ~3.400 km"
    const val TRIP_TRAVELLERS = "Francesco + Giulia"

    val days: List<Day> = listOf(
        Day(
            number = 1,
            date = "Sab 3 ottobre",
            cityShort = "San Francisco",
            title = "Italia → San Francisco + Alcatraz Night Tour",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF1F6FB2,
            segments = listOf(
                Segment("Bologna BLQ › San Francisco SFO", "via Parigi CDG · 13 h 25 di volo · Delta"),
                Segment("SFO › Hotel Zoe (Fisherman's Wharf)", "22 km · 30-45 min · taxi/Uber · \$55-70", "Hotel Zoe 425 North Point St San Francisco"),
                Segment("Hotel Zoe › Pier 33 Alcatraz Landing", "1,1 km · 15 min a piedi", "Pier 33 Alcatraz Landing San Francisco")
            ),
            events = listOf(
                Event("03:15", "Ritrovo aeroporto di Bologna", "Check-in Delta/Air France-KLM (apre 3 h prima). Passaporti + ESTA a portata di mano."),
                Event("06:15", "Volo DL8473 Bologna → Parigi CDG", "1 h 55, arrivo 08:10. Scalo di 2 h 10: cambio terminal per l'intercontinentale."),
                Event("10:20", "Volo DL8726 Parigi → San Francisco", "11 h 30. Arrivo 12:50 ora del Pacifico. Dormire nella seconda metà del volo aiuta col fuso."),
                Event("12:50", "Atterraggio SFO", "Immigrazione ESTA + ritiro bagagli: mettere in conto 60-90 min."),
                Event("14:30", "Trasferimento in hotel", "Taxi/Uber verso Fisherman's Wharf, 22 km, 30-45 min."),
                Event("15:15", "Hotel Zoe Fisherman's Wharf - 425 North Point St", "Camere dalle 16:00: se non pronta, deposito bagagli. Resort fee \$82,40 totale da pagare in loco."),
                Event("16:00", "Doccia + spuntino leggero", "Boudin Bakery al Pier 39 a 5 min. Niente cena pesante: si mangia dopo il tour."),
                Event("17:30", "A piedi verso Pier 33", "1,1 km lungo l'Embarcadero, 15 min."),
                Event("17:45", "Check-in Alcatraz Landing", "Presentarsi 30-45 min prima della partenza: l'imbarco chiude puntuale. QR del biglietto + prenotazione 77243237."),
                Event("18:30", "ALCATRAZ NIGHT TOUR", "Traghetto narrato che circumnaviga l'isola col tramonto (~18:45). Sull'isola: audio tour 'Doing Time' + programmi esclusivi dei ranger.", highlight = true),
                Event("20:40", "Traghetti di rientro", "Rientri ~20:40 e 21:25 (ultimo): orari esposti al molo, si prende quello che si preferisce."),
                Event("21:45", "Cena veloce e a letto", "In-N-Out Burger (333 Jefferson St, aperto fino a tardi). In Italia sono le 6:45 del mattino: crollo autorizzato.")
            ),
            boxes = listOf(
                InfoBox("BIGLIETTO FISSO · Alcatraz Night Tour · 77243237", "Sabato 3/10 ore 18:30, Pier 33. Il vecchio crono lo collocava domenica 4/10: errato (fa fede il biglietto, e di domenica il night tour non opera). Giacca obbligatoria: in baia fa freddo dopo il tramonto.", BoxKind.TICKET),
                InfoBox("eSIM Creo Connect", "Va installata dall'Italia prima di partire; all'atterraggio basta attivare il roaming dati della eSIM. 10 GB inclusi.", BoxKind.TIP)
            ),
            footer = "Hotel Zoe **** · GPS 37.8067, -122.4156 · Notte 1 di 2 a San Francisco · Tramonto ~18:45"
        ),
        Day(
            number = 2,
            date = "Dom 4 ottobre",
            cityShort = "San Francisco",
            title = "San Francisco - Giornata libera in città",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF1F6FB2,
            segments = listOf(
                Segment("Fisherman's Wharf › Golden Gate / Crissy Field", "5 km · 15 min · Uber/bus 28", "Crissy Field Golden Gate Bridge San Francisco")
            ),
            events = listOf(
                Event("07:30", "Colazione al Wharf", "Non inclusa in hotel: Boudin Bakery o caffè sul porto."),
                Event("08:30", "Crissy Field e Golden Gate Bridge", "GPS 37.8023, -122.4490. Passeggiata fronte ponte 1,5-2 h; foto iconiche da Torpedo Wharf.", highlight = true),
                Event("10:30", "Palace of Fine Arts", "A 10 min a piedi da Crissy Field: sosta foto di 30 min."),
                Event("11:45", "Rientro al Wharf e pranzo", "Clam chowder nel pane sourdough, un classico."),
                Event("13:15", "Lombard Street", "La strada più tortuosa d'America, vista dall'incrocio con Hyde St."),
                Event("14:00", "Cable car Powell-Hyde + North Beach", "Corsa storica e caffè nel quartiere italiano (Caffè Trieste)."),
                Event("15:30", "Explorer Pass - 2 attrazioni incluse", "Go City, si attiva al primo utilizzo: consigliati crociera in baia sotto il Golden Gate + Aquarium of the Bay (o Big Bus)."),
                Event("19:00", "Cena", "Ike's Sandwiches (visto in Man vs Food) oppure granchio al Wharf.")
            ),
            boxes = listOf(
                InfoBox("Domani si parte in auto", "Ritiro Hertz alle 9:30 a 3 minuti dall'hotel: valigie pronte stasera, colazione presto. Oggi niente auto: in città si gira meglio con Uber/Muni (il parcheggio dell'hotel costa \$58/giorno).", BoxKind.TIP)
            ),
            footer = "Giornata interamente libera (Alcatraz già fatta ieri) · Notte 2 di 2 a San Francisco"
        ),
        Day(
            number = 3,
            date = "Lun 5 ottobre",
            cityShort = "Sequoia · Bakersfield",
            title = "San Francisco - Sequoia NP - Bakersfield",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF2E7D32,
            segments = listOf(
                Segment("San Francisco (Hertz) › Sequoia - Ash Mountain", "450 km · 4 h 30 · Chevrolet Blazer", "Ash Mountain Entrance Sequoia National Park"),
                Segment("Giant Forest › Bakersfield", "180 km · 2 h 15 · Blazer", "Bakersfield California")
            ),
            events = listOf(
                Event("07:30", "Colazione e check-out", "Bagagli al seguito: non si torna in hotel."),
                Event("09:15", "A piedi da Hertz", "500 Beach St #121, 3 min dall'hotel."),
                Event("09:30", "RITIRO CHEVROLET BLAZER (Hertz)", "Servono: voucher, patente italiana (internazionale consigliata), carta di credito a rilievo intestata a Francesco, biglietto aereo di ritorno. Tariffa COMPLETE: zero franchigia, primo pieno incluso, guidatori extra inclusi - registrare Giulia.", highlight = true),
                Event("10:00", "Partenza verso Sequoia", "US-101 S - I-580 E - CA-99 S - CA-198 E."),
                Event("13:30", "Sosta a Visalia", "Pranzo veloce + PIENO: nel parco la benzina è cara e rara."),
                Event("14:45", "Ash Mountain Entrance", "GPS 36.4752, -118.8295. Mostrare il pass America the Beautiful digitale (QR) + passaporto."),
                Event("15:00", "Generals Highway verso il Giant Forest", "Salita panoramica a tornanti, ~50 min. Parcheggio al Giant Forest Museum."),
                Event("16:00", "General Sherman Tree + Congress Trail", "L'albero più grande del mondo per volume; anello di 1,9 km, ~1 h tra i giganti.", highlight = true),
                Event("17:15", "Moro Rock (se c'è ancora luce)", "400 scalini scavati nella roccia, vista a 360°; 40 min. Evitare col buio."),
                Event("18:00", "Discesa e partenza per Bakersfield", "CA-198 W - CA-65 S."),
                Event("20:15", "Spark by Hilton Bakersfield - 1017 Oak St", "Check-in (fino alle 23). Colazione inclusa. Prenotazione NON rimborsabile."),
                Event("20:45", "Cena", "Wool Growers Restaurant: storica cucina basca di Bakersfield (630 E California Ave).")
            ),
            boxes = listOf(
                InfoBox("Pass parchi 2026 - da comprare PRIMA di partire", "America the Beautiful non-resident \$250 su Recreation.gov (digitale, immediato: salvare il QR offline). Dal 2026 nei parchi principali c'è una sovrattassa di \$100 a persona per i non residenti: il pass la copre per tutto il veicolo. Senza pass, Sequoia + Grand Canyon + Death Valley vi costerebbero ~\$500 in due.", BoxKind.WARNING)
            ),
            footer = "Totale giornata ~630 km · ~7 h di guida · Tempo nel parco ~3 h · Benzina: pieno a Visalia"
        ),
        Day(
            number = 4,
            date = "Mar 6 ottobre",
            cityShort = "Death Valley · Las Vegas",
            title = "Bakersfield - Death Valley - Las Vegas",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFFEF6C00,
            segments = listOf(
                Segment("Bakersfield › Death Valley (Stovepipe Wells)", "365 km · 4 h 15 · Blazer", "Stovepipe Wells Death Valley National Park"),
                Segment("Nel parco › tra i punti panoramici", "~90 km · con soste · Blazer"),
                Segment("Zabriskie Point › Las Vegas", "210 km · 2 h 15 · Blazer", "New York-New York Hotel Las Vegas")
            ),
            events = listOf(
                Event("06:30", "Sveglia e colazione (inclusa)", "Si parte presto per battere il caldo della valle."),
                Event("07:30", "Partenza", "CA-58 E - CA-14 N - US-395 N - CA-190 E."),
                Event("10:30", "PIENO a Olancha o Lone Pine", "Ultima benzina a prezzo umano prima del parco."),
                Event("11:45", "Stovepipe Wells", "GPS 36.6075, -117.1456. Ingresso col pass (Death Valley non ha la sovrattassa 2026)."),
                Event("12:00", "Mesquite Flat Sand Dunes", "Dune scenografiche, 30-40 min. Acqua sempre dietro."),
                Event("13:00", "Furnace Creek Visitor Center + pranzo", "Museo del parco; pranzo al Ranch at Death Valley."),
                Event("14:15", "Badwater Basin", "-85,5 m: il punto più basso del Nord America. 30 min sulla distesa di sale.", highlight = true),
                Event("15:15", "Artists Drive e Artists Palette", "Anello panoramico di 14 km tra colline minerali multicolori, 40 min."),
                Event("16:15", "Zabriskie Point", "Le badlands dorate più fotografate della valle, 25 min."),
                Event("17:00", "Partenza per Las Vegas", "CA-190 E - Death Valley Junction - Pahrump - NV-160."),
                Event("19:15", "New York-New York Hotel & Casino - 3790 S Las Vegas Blvd", "Check-in; resort fee \$51,02/notte in loco. Camera soho king."),
                Event("20:15", "Strip serale + cena", "Hash House A Go Go (Man vs Food), 3535 Las Vegas Blvd: porzioni da denuncia.")
            ),
            boxes = listOf(
                InfoBox("Death Valley a ottobre: 30-35 °C", "Minimo 4 litri d'acqua, crema 50+, cappello, occhiali. Segnale telefonico quasi assente: scaricare le mappe offline la sera prima. Mai allontanarsi dall'auto senza acqua.", BoxKind.WARNING)
            ),
            footer = "Totale giornata ~570 km · ~6 h 30 di guida · Tempo nel parco ~5 h · Dante's View (facolt.): GPS 36.2291, -116.7252"
        ),
        Day(
            number = 5,
            date = "Mer 7 ottobre",
            cityShort = "Route 66 · Williams",
            title = "Las Vegas - Oatman (Route 66) - Williams",
            timezone = "Fuso: si entra in Arizona (MST): stessa ora della California, lancette ferme",
            colorHex = 0xFFC62828,
            segments = listOf(
                Segment("Las Vegas › Oatman, AZ", "240 km · 3 h 00 · Blazer", "Oatman Arizona"),
                Segment("Oatman › Williams (via Sitgreaves Pass)", "165 km · 2 h 15 · Blazer", "Williams Arizona Route 66")
            ),
            events = listOf(
                Event("07:00", "Colazione sulla Strip", "Non inclusa al NYNY."),
                Event("08:00", "Check-out e partenza", "US-93 S: dal Memorial Bridge si vede la Hoover Dam (sosta foto facoltativa, 20 min)."),
                Event("10:30", "PIENO a Kingman", "Nessun distributore tra Kingman e Oatman."),
                Event("11:15", "Oatman - la città di Cars", "GPS 35.0265, -114.3839. Asinelli selvatici liberi in strada, saloon, negozi western; sparatoria-show verso mezzogiorno.", highlight = true),
                Event("12:15", "Pranzo all'Oatman Hotel Restaurant & Saloon", "Pareti tappezzate di dollari; buffalo burger e 'burro ears'."),
                Event("13:30", "Route 66 storica: Sitgreaves Pass", "Tornanti spettacolari sulle Black Mountains: guida lenta, piazzole per foto."),
                Event("14:30", "Kingman - I-40 E - Williams", "Ultimo tratto veloce in interstatale."),
                Event("16:00", "Williams, 'Gateway to Grand Canyon'", "Passeggiata sul centro storico Route 66: neon, diner anni '50, negozi vintage."),
                Event("16:45", "SureStay by Best Western - 800 E Route 66", "Check-in; colazione continentale inclusa, camera con frigo e microonde."),
                Event("18:30", "Cena", "Cruiser's Route 66 Cafe: ribs affumicati, birre artigianali, musica dal vivo.")
            ),
            boxes = listOf(
                InfoBox("Oatman e il film Cars", "Oatman ha ispirato Radiator Springs: paese di minatori tagliato fuori dalla costruzione della Interstate 40, esattamente come nel film. Gli asinelli discendono da quelli dei cercatori d'oro.", BoxKind.TIP)
            ),
            footer = "Totale giornata ~405 km · ~5 h 15 di guida · Domani sveglia alle 5:15 · Tempo a Oatman ~2 h 15"
        ),
        Day(
            number = 6,
            date = "Gio 8 ottobre",
            cityShort = "Grand Canyon · M. Valley",
            title = "Alba al Grand Canyon - Monument Valley",
            timezone = "Fuso: Grand Canyon MST · dal pomeriggio Nazione Navajo (MDT): lancette +1 h",
            colorHex = 0xFFB5451B,
            segments = listOf(
                Segment("Williams › Mather Point (Grand Canyon)", "95 km · 1 h 00 · Blazer", "Mather Point Grand Canyon"),
                Segment("Desert View Drive › panoramica nel parco", "42 km · 1 h 30 con soste · Blazer", "Desert View Watchtower Grand Canyon"),
                Segment("Desert View › Monument Valley (The View)", "280 km · 3 h 00 · Blazer", "The View Hotel Monument Valley")
            ),
            events = listOf(
                Event("05:15", "Sveglia", "Vestirsi a strati: all'alba, a 2.100 m, ci sono 0-5 °C."),
                Event("05:40", "Partenza per il Grand Canyon", "AZ-64 N, strada veloce e buia: occhio agli alci."),
                Event("06:15", "Mather Point", "Parcheggio al Visitor Center, 5 min a piedi dal bordo. Posizionarsi 15 min prima."),
                Event("06:30", "ALBA SUL GRAND CANYON", "Il canyon si accende di rosso e oro: lo spettacolo del viaggio.", highlight = true),
                Event("07:30", "Rim Trail: Mather - Yavapai Point", "1,5 km sul bordo + Geology Museum (gratuito), viste da vertigine."),
                Event("09:00", "Desert View Drive", "42 km panoramici: soste a Grandview Point, Moran Point, Lipan Point."),
                Event("10:45", "Desert View Watchtower", "Torre storica di Mary Colter con murales Hopi, vista a 360° sul fiume."),
                Event("11:30", "Partenza per Monument Valley", "AZ-64 - US-89 N - US-160 E - US-163. A Tuba City lancette AVANTI di 1 h (ora Navajo)."),
                Event("15:30", "The View Hotel (ora Navajo)", "Check-in: camera con vista diretta sui monoliti. Resort fee \$21,19 GIÀ inclusa nella tariffa: non va ripagata."),
                Event("16:15", "Valley Drive", "Sterrata di 27 km tra Mittens, Merrick ed Elephant Butte: 2-2,5 h, il Blazer basta e avanza.", highlight = true),
                Event("18:50", "Tramonto sui monoliti", "Dalla terrazza dell'hotel o da John Ford's Point: rosso Navajo puro."),
                Event("19:45", "Cena al The View Restaurant", "Cucina navajo con vetrata sui buttes: provare il taco navajo.")
            ),
            boxes = listOf(
                InfoBox("Da qui l'orologio va avanti di un'ora", "La Nazione Navajo osserva l'ora legale, il resto dell'Arizona no: Monument Valley è a +1 h rispetto a Williams/Page. Telefoni e navigatore possono impazzire ai confini: fanno fede gli orari locali di questo crono. Ingresso Monument Valley: \$8 a persona (parco tribale: il pass federale NON vale).", BoxKind.WARNING)
            ),
            footer = "Totale ~415 km · ~5 h 30 di guida · Alba 06:30 (MST) · tramonto 18:50 (ora Navajo) · Pieno a Kayenta prima di salire al parco"
        ),
        Day(
            number = 7,
            date = "Ven 9 ottobre",
            cityShort = "Antelope · Lake Powell",
            title = "Antelope Canyon (ore 11:30) - Lake Powell",
            timezone = "Fuso: arrivando a Page lancette INDIETRO di 1 h (Page = ora della California)",
            colorHex = 0xFF6A1B9A,
            segments = listOf(
                Segment("The View (Monument Valley) › Ken's Tours (Page)", "200 km · 2 h 00 · Blazer", "Ken's Tours Lower Antelope Canyon Page Arizona"),
                Segment("Ken's Tours › Horseshoe Bend e resort", "20 km · spostamenti brevi · Blazer", "Lake Powell Resort Page Arizona")
            ),
            events = listOf(
                Event("07:00", "Colazione con vista sui monoliti + check-out", "Al The View Restaurant (pasti a proprio carico)."),
                Event("08:15", "Partenza per Page (ora Navajo)", "US-163 - US-160 W - AZ-98 W, attraverso Kayenta."),
                Event("09:20", "Arrivo a Page (ora locale)", "Col cambio di fuso si 'guadagna' un'ora: benzina, caffè e zero fretta."),
                Event("10:30", "Check-in da Ken's Tours - Indian Rte 222", "10 min da Page. Arrivare 1 h prima: chi non ha fatto check-in 30 min prima del tour perde il posto. Ammessi solo telefono, 1 fotocamera e bottiglietta d'acqua: niente zaini, GoPro, treppiedi, video. Contanti per la mancia alla guida (\$3-5 a testa)."),
                Event("11:30", "LOWER ANTELOPE CANYON", "Prenotazione KEN-346819. Discesa nel canyon con guida navajo: ~1 h 15 tra scale e onde di arenaria illuminate.", highlight = true),
                Event("13:00", "Pranzo a Page", "Big John's Texas BBQ (stile Man vs Food) o State 48 Tavern."),
                Event("14:15", "Horseshoe Bend", "Parcheggio \$10; 1,2 km a piedi fino al bordo sull'ansa del Colorado. Luce perfetta nel primo pomeriggio: 1 h.", highlight = true),
                Event("15:30", "Lake Powell Resort - 100 Lakeshore Dr", "Check-in; resort fee \$14,99/notte in loco (include navetta, palestra, piscina). Camera 2 letti queen."),
                Event("16:00", "Pomeriggio sul lago", "Kayak \$30/h o paddle \$25/h, oppure relax in spiaggia; foto dal Wahweap Overlook (GPS 36.9948, -111.4980)."),
                Event("18:30", "Tramonto sul lago + cena", "Rainbow Room con vetrata panoramica sul Powell.")
            ),
            boxes = listOf(
                InfoBox("BIGLIETTO FISSO · Ken's Tours Lower Antelope · KEN-346819", "Venerdì 9/10 ore 11:30 (ora di Page), 2 persone, \$161 già pagati. Il vecchio crono indicava le 10:00: fa fede il biglietto. Ritardo = posto perso, senza rimborso. L'escursione dell'agenzia (prevista il 10/10) risulta annullata con penale e sostituita da questa prenotazione diretta.", BoxKind.TICKET)
            ),
            footer = "Totale ~220 km · ~2 h 30 di guida · Buffet colazione del resort \$21 (non incluso) · Pomeriggio pieno a Page e sul lago"
        ),
        Day(
            number = 8,
            date = "Sab 10 ottobre",
            cityShort = "Lake Powell · Las Vegas",
            title = "Lake Powell - Las Vegas",
            timezone = "Fuso: breve tratto in Utah (+1 h solo sui telefoni), poi Nevada: ora di partenza",
            colorHex = 0xFFAD1457,
            segments = listOf(
                Segment("Page › Las Vegas (Caesars)", "450 km · 4 h 15 · Blazer", "Caesars Palace Las Vegas")
            ),
            events = listOf(
                Event("08:00", "Colazione con calma", "Buffet del resort \$21 a testa (facoltativo) o caffè al market."),
                Event("09:15", "Check-out e partenza", "US-89 S/W - Kanab - UT-59/AZ-389 - Hurricane - I-15 S."),
                Event("11:30", "Sosta panoramica: Virgin River Gorge", "La I-15 taglia un canyon spettacolare tra Arizona e Nevada: area di sosta per foto."),
                Event("13:45", "CAESARS PALACE - 3570 Las Vegas Blvd S", "Check-in: tipologia camera assegnata all'arrivo; resort fee giornaliera in loco (~\$50)."),
                Event("15:00", "Strip a piedi", "Bellagio Conservatory (gratis) - Paris - The Venetian con i canali interni (gondola \$34 facolt.)."),
                Event("17:30", "Facoltativo", "High Roller, la ruota da 168 m, oppure l'esterno della Sphere."),
                Event("19:00", "Fountains of Bellagio", "Spettacoli ogni 30 min, ogni 15 in serata, fino a mezzanotte: piazzarsi sulla balaustra centrale.", highlight = true),
                Event("20:30", "Cena", "Bacchanal Buffet al Caesars (il top di Vegas: prenotare) oppure ancora Hash House.")
            ),
            boxes = listOf(
                InfoBox("Niente più vulcano", "Il Volcano Show del Mirage non esiste più: l'hotel ha chiuso nel 2024 per diventare Hard Rock. Gli spettacoli gratuiti restano le fontane del Bellagio e la Sphere illuminata.", BoxKind.TIP),
                InfoBox("Benzina per domani", "Stasera o domattina presto fare il pieno vicino allo Strip: domani 435 km fino a LAX. Il Blazer si riconsegna anche con serbatoio quasi vuoto (primo pieno incluso nella tariffa COMPLETE): niente pieno finale.", BoxKind.TIP)
            ),
            footer = "Totale ~450 km · ~4 h 15 di guida · Pomeriggio e sera sulla Strip · Domani sveglia 6:15"
        ),
        Day(
            number = 9,
            date = "Dom 11 ottobre",
            cityShort = "Los Angeles",
            title = "Las Vegas - Los Angeles (diretta) + cambio auto",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF3949AB,
            segments = listOf(
                Segment("Caesars Palace › LAX Rental Car Center", "435 km · 4 h 15 · Blazer", "LAX Rental Car Center 5251 W 98th St Los Angeles"),
                Segment("Rental Car Center › Hollywood Volume", "25 km · 30-45 min · Ford Mustang cabrio", "Hollywood Volume 6516 W Selma Ave Los Angeles")
            ),
            events = listOf(
                Event("06:15", "Sveglia e check-out rapido", "Colazione veloce da portare."),
                Event("06:45", "Pieno vicino allo Strip", "Servono 435 km di autonomia (il pieno finale invece non serve: incluso in tariffa)."),
                Event("07:00", "Partenza: I-15 S - I-10 W - I-405 S", "Di domenica mattina si viaggia; il rientro pomeridiano dei weekender intaserebbe il Cajon Pass. Palm Springs saltata di proposito per rispettare gli orari di contratto."),
                Event("11:15", "LAX RENTAL CAR CENTER - 5251 W 98th St", "Dal 2026 tutti i noleggi di LAX (Hertz e Alamo inclusi) sono in questo unico edificio: seguire i cartelli 'Rental Car Return'."),
                Event("11:30", "RICONSEGNA BLAZER (Hertz)", "Entro le 12:00 come da contratto. Foto all'auto e ricevuta di chiusura. Il voucher riporta la vecchia sede di 9000 Airport Blvd: confermare al ritiro di San Francisco, ma la riconsegna ormai avviene al Rental Car Center.", highlight = true),
                Event("12:00", "RITIRO FORD MUSTANG CABRIO (Alamo)", "Stesso edificio, banco Alamo: stessa carta e patente. Tariffa FULLY con 1 guidatore aggiuntivo incluso: registrare Giulia. CHIEDERE AL BANCO di spostare a contratto la riconsegna del 14/10 dalle 11:00 alle ~14:30 e farsi quotare le ore extra.", highlight = true),
                Event("12:45", "Pranzo da In-N-Out di Sepulveda", "9149 S Sepulveda Blvd: quello famoso con gli aerei che atterrano sopra la testa."),
                Event("13:45", "Verso Hollywood", "I-405 N - US-101, capote giù se il traffico lo consente."),
                Event("14:30", "HOLLYWOOD VOLUME - 6516 W Selma Ave", "Check-in (se la camera non è pronta: deposito bagagli). Mandatory fee \$104,58; parcheggio \$35/giorno."),
                Event("15:30", "Hollywood Boulevard", "Walk of Fame, TCL Chinese Theatre, Dolby Theatre: 2 h a piedi."),
                Event("17:45", "Griffith Observatory", "Parcheggio \$10/h o navetta DASH. Tramonto ~18:20 sull'insegna HOLLYWOOD e su tutta LA; ingresso gratuito, aperto fino alle 22.", highlight = true),
                Event("20:30", "Cena a Hollywood", "Musso & Frank Grill (dal 1919) o zona Highland.")
            ),
            boxes = listOf(
                InfoBox("Promemoria al banco Alamo", "1) Riconsegna del 14/10 spostata a ~14:30, messa a contratto. 2) Giulia registrata come guidatrice aggiuntiva (inclusa). 3) Prova bagagliaio subito: le due valigie da 23 kg nella Mustang entrano giuste giuste, meglio trovare l'incastro adesso.", BoxKind.WARNING)
            ),
            footer = "Totale ~460 km · ~5 h di guida · Mezza giornata guadagnata su Los Angeles · Griffith: la domenica apre alle 12"
        ),
        Day(
            number = 10,
            date = "Lun 12 ottobre",
            cityShort = "Universal Studios",
            title = "Universal Studios Hollywood (Express)",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF0277BD,
            segments = listOf(
                Segment("Hollywood Volume › Universal Studios", "13 km · 20-40 min · Mustang", "Universal Studios Hollywood")
            ),
            events = listOf(
                Event("07:30", "Colazione abbondante", "Giornata lunga in piedi."),
                Event("08:15", "Partenza", "US-101 N, uscita Universal Studios Blvd. Parcheggio General \$30, Preferred \$50."),
                Event("09:00", "Apertura del parco", "Biglietto EXPRESS (accesso rapido 1 volta per attrazione) + documento d'identità con foto obbligatorio. Verificare l'orario del giorno sull'app 'Universal Studios Hollywood'."),
                Event("09:30", "Studio Tour per primo", "60 min nei veri set: King Kong 360 3D, Fast & Furious, Jaws.", highlight = true),
                Event("11:00", "The Wizarding World of Harry Potter", "Forbidden Journey, Flight of the Hippogriff, butterbeer a Hogsmeade."),
                Event("13:00", "Pranzo nel parco", "Three Broomsticks (a tema Harry Potter) o Jurassic Cafe, \$20-25 a testa."),
                Event("14:30", "Super Nintendo World", "Mario Kart: Bowser's Challenge, Yoshi's Adventure, sfide interattive."),
                Event("17:00", "Transformers, Jurassic World, WaterWorld", "Lo show acquatico WaterWorld di solito alle 17:30: arrivare 15 min prima."),
                Event("19:00", "CityWalk", "Cena e negozi nella zona gratuita all'ingresso; dolce da Voodoo Doughnut."),
                Event("21:30", "Rientro in hotel", "Riposo: domani giornata di spiagge.")
            ),
            boxes = listOf(
                InfoBox("BIGLIETTO FISSO · Universal Express, 12 ottobre", "Non rimborsabile e valido solo oggi; ai tornelli serve un documento con foto insieme al biglietto. Con l'Express si risparmiano 60-120 min a coda: usarlo su ogni attrazione.", BoxKind.TICKET)
            ),
            footer = "Guida quasi nulla oggi (26 km A/R) · App del parco per i tempi d'attesa"
        ),
        Day(
            number = 11,
            date = "Mar 13 ottobre",
            cityShort = "Malibu · LA Beaches",
            title = "Beverly Hills - Venice - Malibu (tramonto)",
            timezone = "Fuso: PDT (UTC-7) · Italia -9 h",
            colorHex = 0xFF00838F,
            segments = listOf(
                Segment("Anello spiagge › BH - Venice - Malibu - Hollywood", "~130 km · giornata · Mustang, capote giù", "El Matador State Beach Malibu California")
            ),
            events = listOf(
                Event("08:30", "Partenza", "Santa Monica oggi si salta apposta: è in programma domattina, prima del volo."),
                Event("09:00", "Beverly Hills e Rodeo Drive", "Via Rodeo, Beverly Hills Hotel (esterno): 1 h tra le vetrine."),
                Event("10:30", "Venice Beach + Venice Canals", "Muscle Beach, skate park, artisti di strada; poi i canali 'italiani'. Pranzo su Abbot Kinney Blvd."),
                Event("13:30", "Pacific Coast Highway verso nord", "Capote giù, oceano a sinistra: la guida più bella del viaggio.", highlight = true),
                Event("14:15", "Malibu Pier e Surfrider Beach", "Molo storico, surfisti, Malibu Lagoon: 45 min."),
                Event("15:30", "El Matador State Beach", "Parcheggio \$8, scala ripida: faraglioni e grotte marine, la spiaggia più fotogenica di LA."),
                Event("18:15", "TRAMONTO SUL PACIFICO", "Golden hour tra gli scogli di El Matador.", highlight = true),
                Event("19:15", "Cena a Malibu", "Malibu Farm sul molo oppure Malibu Seafood (fish market sulla PCH)."),
                Event("20:45", "Rientro a Hollywood", "PCH - I-10 E - US-101 N, ~50 min.")
            ),
            boxes = listOf(
                InfoBox("Nota PCH", "Dopo gli incendi del 2025 alcuni tratti della Pacific Coast Highway possono avere cantieri o limiti: se Maps segnala chiusure, ripiegare su Zuma Beach. La storica Reel Inn non esiste più.", BoxKind.WARNING),
                InfoBox("Stasera: valigie", "Domani è il check-out definitivo: chiudere le valigie stasera e tenere fuori solo l'essenziale del volo.", BoxKind.TIP)
            ),
            footer = "~130 km di anello costiero · Tramonto ~18:15 · Ultima notte a Los Angeles"
        ),
        Day(
            number = 12,
            date = "Mer 14 ottobre",
            cityShort = "Santa Monica · Volo",
            title = "Santa Monica - LAX - volo per l'Italia",
            timezone = "Fuso: PDT (UTC-7) · in volo lancette avanti di 9 h",
            colorHex = 0xFF00897B,
            segments = listOf(
                Segment("Hollywood › Santa Monica Pier", "25 km · 40-50 min · Mustang", "Santa Monica Pier California"),
                Segment("Santa Monica › LAX Rental Car Center", "14 km · 25-35 min + benzina · Mustang", "LAX Rental Car Center 5251 W 98th St Los Angeles")
            ),
            events = listOf(
                Event("08:00", "Colazione e CHECK-OUT", "Valigie nel bagagliaio chiuso, nulla in vista, capote su nei parcheggi."),
                Event("08:45", "Verso Santa Monica", "I-405 S o Santa Monica Blvd."),
                Event("09:45", "Santa Monica Pier e Palisades Park", "Ruota panoramica solare, passeggiata sulla spiaggia: ultimo oceano del viaggio. Parcheggio: Lot 1 North o strutture del centro."),
                Event("12:00", "PRANZO vicino al molo", "Big Dean's Ocean Front Cafe (istituzione fronte spiaggia dal 1902) oppure The Albright sul molo (seafood)."),
                Event("13:10", "Partenza + PIENO OBBLIGATORIO", "Tariffa FULLY = restituzione col pieno. Distributori su Lincoln Blvd/Sepulveda, non dentro il perimetro LAX.", highlight = true),
                Event("13:50", "RICONSEGNA MUSTANG - Rental Car Center, 5251 W 98th St", "Orario ~14:30 concordato a contratto al ritiro dell'11/10. Controllo danni, ricevuta, sblocco cauzione.", highlight = true),
                Event("14:15", "Verso il terminal", "Treno SkyLink (in servizio dal 2026: ~10 min, corse ogni 2 min) oppure navetta Alamo per il Tom Bradley International."),
                Event("14:40", "TOM BRADLEY INTERNATIONAL (TBIT)", "Check-in Delta: 1 bagaglio da 23 kg a testa. Consigliate 3 h prima (15:25): siete in anticipo."),
                Event("15:30", "Sicurezza e controllo documenti", "Poi duty free e cena leggera in area gate."),
                Event("17:40", "Imbarco", "Il gate chiude ~20 min prima della partenza."),
                Event("18:25", "Volo DL8727 Los Angeles → Parigi CDG", "10 h 50, pasti e notte a bordo. Arrivo il 15/10 alle 14:15 ora francese.", highlight = true)
            ),
            boxes = listOf(
                InfoBox("Checklist di partenza", "Pieno fatto e scontrino conservato · ricevuta di riconsegna auto · passaporti + ESTA · carte d'imbarco · souvenir fragili nel bagaglio a mano · max 23 kg a valigia · powerbank sempre in cabina, mai in stiva.", BoxKind.WARNING)
            ),
            footer = "~40 km · ~1 h 15 di guida · Mattinata intera a Santa Monica · In terminal ~3 h 45 prima del volo"
        ),
        Day(
            number = 13,
            date = "Gio 15 ottobre",
            cityShort = "Rientro Italia",
            title = "Rientro in Italia",
            timezone = "Fuso: Parigi/Bologna CEST (UTC+2)",
            colorHex = 0xFF546E7A,
            segments = emptyList(),
            events = listOf(
                Event("14:15", "Atterraggio a Parigi CDG", "SCALO DI SOLI 70 MINUTI: controllo passaporti Schengen ed eventuale cambio sala. Muoversi subito, niente soste.", highlight = true),
                Event("15:25", "Volo DL8421 Parigi → Bologna", "1 h 40."),
                Event("17:05", "Atterraggio a Bologna", "Bentornati: il jet lag al contrario (-9 h) si smaltisce in 3-4 giorni di luce mattutina.", highlight = true)
            ),
            boxes = listOf(
                InfoBox("Se la coincidenza salta", "Assistenza Delta in aeroporto per la riprotezione + CREO h24: +39 0721 1748058. Al rientro, tutela legale gratuita per ritardi oltre 3 h: info@dirittoeturismo.it citando la pratica 26/008376.", BoxKind.WARNING)
            ),
            footer = "Essenziali (documenti, farmaci, caricatori) nel bagaglio a mano"
        )
    )

    /** CHECKLIST PRIMA DI PARTIRE (pag. 15 del cronoprogramma). */
    val departureChecklist: List<ChecklistItem> = listOf(
        ChecklistItem("pass_parchi", "Pass America the Beautiful non-resident (\$250) comprato su Recreation.gov: QR salvato offline + passaporto al gate dei parchi"),
        ChecklistItem("esta", "ESTA validi e passaporti con scadenza oltre il rientro"),
        ChecklistItem("esim", "eSIM Creo Connect installata DALL'ITALIA (all'arrivo basta attivare il roaming dati)"),
        ChecklistItem("patente", "Patente + patente internazionale (consigliata) + carta di credito A RILIEVO intestata a Francesco (no prepagate/elettroniche)"),
        ChecklistItem("qr_conferme", "QR e conferme: Alcatraz 77243237 · Ken's KEN-346819 · Explorer Pass · Universal Express (+ documento con foto)"),
        ChecklistItem("mappe_offline", "Mappe offline su Google Maps: Death Valley, Nazione Navajo, tratte nel deserto"),
        ChecklistItem("contanti", "Contanti piccoli: mance (10-15% al ristorante, \$3-5 alla guida di Antelope), parcheggi spiagge"),
        ChecklistItem("depositi", "Depositi su carta: Hertz \$200 + hotel ~\$50/notte (si sbloccano entro ~20 giorni)"),
        ChecklistItem("giacca", "Giacca calda (Alcatraz di sera, alba al Grand Canyon), strati, crema 50+, borraccia grande"),
        ChecklistItem("numeri_utili", "Numeri utili: CREO h24 +39 0721 1748058 · Unipol Assistance +39 011 6523 211 (tessera 2127 2012)")
    )

    const val ASSISTANCE = "CREO assistenza h24: +39 0721 1748058 (pratica 26/008376) · Unipol Assistance: +39 011 6523 211 (tessera 2127 2012) · Tutela legale ritardi voli: info@dirittoeturismo.it"
}
