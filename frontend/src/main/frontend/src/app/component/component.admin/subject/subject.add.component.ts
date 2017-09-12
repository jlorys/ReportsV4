import {Component, EventEmitter, Input, OnDestroy, Output} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message, SelectItem} from "primeng/primeng";
import {Subject} from "./subject";
import {Laboratory} from "../laboratory/laboratory";
import {SubjectDataService} from "./subject.data.service";
import {FieldOfStudyDataService} from "../fieldofstudy/fieldofstudy.data.service";
import {FieldOfStudy} from "../fieldofstudy/fieldofstudy";

@Component({
  templateUrl: 'subject.add.component.html',
  selector: 'subjects-add',
})
export class SubjectAddComponent implements OnDestroy {

  @Input() header = "Laboratoria z tego przedmiotu...";
  subject: Subject;
  subjectLaboratories: Laboratory[] = [];

  private params_subscription: any;

  @Input() sub: boolean = false;
  @Output() onSaveClicked = new EventEmitter<Subject>();

  msgs: Message[] = [];

  fieldOfStudy: FieldOfStudy;
  sourceFieldsOfStudiesSelectItems: SelectItem[];

  constructor(private route: ActivatedRoute, protected router: Router, private subjectDataService: SubjectDataService, private fieldOfStudyDataService: FieldOfStudyDataService) {
    this.sourceFieldsOfStudiesSelectItems = [];
    this.sourceFieldsOfStudiesSelectItems.push({label: '--------------------------', value: null});

    if (this.sub) {
      return;
    }

    fieldOfStudyDataService.findAll().subscribe(fieldOfStudy => fieldOfStudy.forEach((value, index, array) => this.sourceFieldsOfStudiesSelectItems.push({
        label: value.name,
        value: value
      })),
      error => this.msgs.push({
        severity: 'error',
        summary: 'Constructor subject fields of studies error',
        detail: error
      }));

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for Subject-add ' + id);
      if (id === 'add') {
        this.subject = new Subject();
        this.fieldOfStudy = null;
      } else {
        this.subjectDataService.getSubject(id)
          .subscribe(subject => {
              this.subject = subject;
              this.subjectLaboratories = subject.laboratories;
              this.fieldOfStudy = subject.fieldOfStudy;
            },
            error => this.msgs.push({severity: 'error', summary: 'Constructor error', detail: error})
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
    this.msgs = []; //this line fix disappearing of messages

    this.subject.fieldOfStudy = this.fieldOfStudy;

    this.subjectDataService.update(this.subject).subscribe(
      subject => {
        this.subject = subject;

        if (this.sub) {
          this.onSaveClicked.emit(this.subject);
        } else {
          this.msgs.push({severity: 'info', summary: 'Zapisano', detail: 'OK!'})
        }
      },
      error => this.msgs.push({severity: 'error', summary: 'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event: any) {
    let id = event.data.id;
    this.router.navigate(['/laboratories', id]);
  }
}
