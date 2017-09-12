import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message, SelectItem} from "primeng/primeng";
import {Laboratory} from "./laboratory";
import {Report} from "../report/report";
import {LaboratoryDataService} from "./laboratory.data.service";
import {SubjectDataService} from "../subject/subject.data.service";
import {Subject} from "../subject/subject";

@Component({
  templateUrl: 'laboratory.add.component.html',
  selector: 'laboratories-add',
})
export class LaboratoryAddComponent implements OnInit, OnDestroy {

  @Input() header = "Sprawozdania z tego laboratorium...";
  laboratory : Laboratory;
  laboratoryReports: Report[] = [];

  private params_subscription: any;

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<Laboratory>();

  msgs: Message[] = [];

  subject: Subject;
  sourceSubjectsSelectItems: SelectItem[];

  labDate: Date;
  returnReportDate: Date;
  finalReturnReportDate: Date;

  constructor(private route: ActivatedRoute, protected router: Router, private laboratoryDataService: LaboratoryDataService, private subjectDataService: SubjectDataService) {
    this.sourceSubjectsSelectItems = [];
    this.sourceSubjectsSelectItems.push({label: '--------------------------', value: null});

    if (this.sub) {
      return;
    }

    subjectDataService.findAll().subscribe(subject => subject.forEach((value, index, array) => this.sourceSubjectsSelectItems.push({
        label: value.name,
        value: value
      })),
      error => this.msgs.push({
        severity: 'error',
        summary: 'Constructor subject of laboratory error',
        detail: error
      }));

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for Laboratory-add ' + id);
      if (id === 'add') {
        this.laboratory = new Laboratory();
        this.subject = null;
        this.labDate = null;
        this.returnReportDate = null;
        this.finalReturnReportDate = null;
      } else {
        this.laboratoryDataService.getLaboratory(id)
          .subscribe(laboratory => {
              this.laboratory = laboratory;
              this.laboratoryReports = laboratory.reports;
              this.subject = laboratory.subject;
              this.labDate = new Date(laboratory.labDate);
              this.returnReportDate = new Date(laboratory.returnReportDate);
              this.finalReturnReportDate = new Date(laboratory.finalReturnReportDate);
            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  pl: any;
  ngOnInit() {
    this.pl = {
      firstDayOfWeek: 1,
      dayNamesMin: [ "ndz","pn","wt","śr","cz","pt","sob" ],
      monthNames: [ "styczeń","luty","marzec","kwiecień","maj","czerwiec","lipiec","sierpień","wrzesień","październik","listopad","grudzień" ],
      today: 'Dzisiaj',
      clear: 'Wyczyść'
    }
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onSave() {
    this.msgs = []; //this line fix disappearing of messages

    if(this.labDate){
      this.laboratory.labDate = this.labDate.getTime();
    }
    if(this.returnReportDate){
      this.laboratory.returnReportDate = this.returnReportDate.getTime();
    }
    if(this.finalReturnReportDate) {
      this.laboratory.finalReturnReportDate = this.finalReturnReportDate.getTime();
    }

    this.laboratory.subject = this.subject;

    this.laboratoryDataService.update(this.laboratory).
    subscribe(
      laboratory => {
        this.laboratory = laboratory;
        if (this.sub) {
          this.onSaveClicked.emit(this.laboratory);
        } else {
          this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})
        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reports', id]);
  }
}
