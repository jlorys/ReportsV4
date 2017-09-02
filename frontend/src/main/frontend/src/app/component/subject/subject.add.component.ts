import {Component, EventEmitter, Input, OnDestroy, Output} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {Subject} from "../subject/subject";
import {Laboratory} from "../laboratory/laboratory";
import {SubjectDataService} from "./subject.data.service";

@Component({
  templateUrl: 'subject.add.component.html',
  selector: 'subjects-add',
})
export class SubjectAddComponent implements OnDestroy {

  @Input() header = "Przedmioty tego kierunku studiów...";
  subject : Subject;
  subjectLaboratories: Laboratory[] = [];

  private params_subscription: any;

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<Subject>();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private subjectDataService: SubjectDataService) {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for Subject-add ' + id);
      if (id === 'add') {
        this.subject = new Subject();

      } else {
        this.subjectDataService.getSubject(id)
          .subscribe(subject => {
              this.subject = subject;
              this.subjectLaboratories = subject.laboratories;
            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onSave() {
    this.subjectDataService.update(this.subject).
    subscribe(
      subject => {
        this.subject = subject;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.subject);
        } else {
          this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})
        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/laboratories', id]);
  }
}
