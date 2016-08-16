var influunt;
(function (influunt) {
  var components;
  (function (components) {
    var QuadroTabelaHorario = (function () {
      function QuadroTabelaHorario(dias, eventos) {
        this.diasDaSemana = ['dom','seg','ter','qua','qui','sex','sab'];
        this.agenda = {};
        this.dias = dias;
        this.eventos = eventos;
        this.intervaloHora = 4;
        this.intervaloDia = 96;
        this.horaDia = 24;
      }
      QuadroTabelaHorario.prototype.calcula = function () {
        this.agenda = this.inicializa();
        var eventos = this.eventos;
        var programas = [];
        var index = 0;
        eventos.forEach(function(evento){
          var programa = _.cloneDeep(evento);
          if(programa.diaDaSemana && programa.horario){
            programa.hora = programa.horario.split(':')[0];
            programa.minuto = programa.horario.split(':')[1];
            programa.dia = programa.diaDaSemana;
            programa.class = 'horarioColor' + (index+1);
            programa.index = index;
            index++;
            programas.push(programa);
          }
        });
        var trocas = this.getTrocas(programas)
        var ultimo = undefined;
        ultimo = this.passada(trocas,ultimo);
        this.passada(trocas,ultimo);
        this.intervalos()
        return this.agenda;
      };
      QuadroTabelaHorario.prototype.inicializa = function () {
        var agenda = this.agenda;
        var diasDaSemana = this.diasDaSemana;
        var intervaloHora = this.intervaloHora;
        var horaDia = this.horaDia;
        for(var minuto = 0; minuto < intervaloHora; minuto++){
          for(var hora = 0; hora < horaDia; hora++ ){
            diasDaSemana.forEach(function(dia){
              if(typeof agenda[dia] === 'undefined'){
                agenda[dia] =  new Array(horaDia);
                for(var i = 0; i < horaDia; i++){
                  agenda[dia][i] = new Array(intervaloHora);
                }
              }
              agenda[dia][hora][minuto] = {state: 'unset'};
            });
          }
        }
        return agenda;
      };
      QuadroTabelaHorario.prototype.getTrocas = function (programas) {
        var hash = {};
        var dias = this.dias;
        programas.forEach(function(programa){
          var hora = parseInt(programa.hora);
          var minuto = parseInt(programa.minuto / 15);
          var dia = _.find(dias, {value: programa.dia});
          dia.dias.forEach(function(dia){
            hash[dia] = hash[dia] || {}
            hash[dia][hora] = hash[dia][hora] || {}
            if(hash[dia][hora][minuto]){
              if(comparePrograma(programa,hash[dia][hora][minuto]) < 0){
                hash[dia][hora][minuto] = programa
              }
            }else{
              hash[dia][hora][minuto] = programa
            }
          
          })
        });
        return hash;
      };
      QuadroTabelaHorario.prototype.passada = function (trocas, ultimo) {
        var agenda = this.agenda;
        var diasDaSemana = this.diasDaSemana;
        var intervaloHora = this.intervaloHora;
        var intervaloDia = this.intervaloDia;
        if(Object.keys(trocas).length > 0){
          diasDaSemana.forEach(function(dia){
            for(var i = 0; i < intervaloDia; i++){
              var hora = Math.floor(i / intervaloHora);
              var minuto = i % intervaloHora;
              var slot = agenda[dia][hora][minuto];
        
              if(slot.state == 'unset'){
                if(trocas[dia] && trocas[dia][hora] && trocas[dia][hora][minuto]){
                  slot.state = trocas[dia][hora][minuto].class;
                  slot.index = trocas[dia][hora][minuto].index;
                  ultimo = slot.state;
                }else if(typeof ultimo !== 'undefined'){
                  slot.state = ultimo;
                }
              }
            }
          });
        }
        return ultimo;
      };
      QuadroTabelaHorario.prototype.intervalos = function () {
        var intervalo = {};
        var agenda = this.agenda;
        var diasDaSemana = this.diasDaSemana;
        var intervaloDia = this.intervaloDia;
        var intervaloHora = this.intervaloHora;
      
        if(agenda['dom'][0][0].state === 'unset'){
          return;
        }
      
        var currentIndex = undefined;
      
        diasDaSemana.forEach(function(dia){
          for(var i = 0; i < intervaloDia; i++){
            var hora = Math.floor(i / intervaloHora);
            var minuto = i % intervaloHora;
            var slotIndex = agenda[dia][hora][minuto].index;
            if(typeof slotIndex === 'undefined'){
              slotIndex = currentIndex;
            }

            if(typeof currentIndex === 'undefined'){
              currentIndex = slotIndex;
              intervalo[currentIndex] = intervalo[currentIndex] || [];
              intervalo[currentIndex].push([[dia,hora,minuto],[dia,hora,minuto]])
            }else{
              if(currentIndex !== slotIndex){
                currentIndex = slotIndex;
                intervalo[currentIndex] = intervalo[currentIndex] || [];
                intervalo[currentIndex].push([[dia,hora,minuto],[dia,hora,minuto]])
              }else{
                var last = intervalo[currentIndex][intervalo[currentIndex].length - 1];
                last[1][0]= dia;
                last[1][1]= hora;
                last[1][2]= minuto; 
              }
            }
          }
        });
        return intervalo;
      };
      QuadroTabelaHorario.prototype.comparePrograma = function (a, b) {
        var pa = JSON.parse(a.dia).prioridade;
        var pb = JSON.parse(b.dia).prioridade;
        var ha = parseInt(a.hora);
        var hb = parseInt(b.hora);
        var ma = parseInt(a.minuto);
        var mb = parseInt(b.minuto);
  
        if (pa < pb ) {
          return -1;
        }else if(pa > pb){
          return 1;
        }else{
          if(ha < hb){
            return -1;
          }else if(ha > hb){
            return 1
          }else{
            if(ma < mb){
              return -1;
            }else if(ma > mb){
              return 1;
            }
          }
        }
        return 0;
      };

      return QuadroTabelaHorario;
    }());
    components.QuadroTabelaHorario = QuadroTabelaHorario;
  })(components = influunt.components || (influunt.components = {}));
})(influunt || (influunt = {}));
