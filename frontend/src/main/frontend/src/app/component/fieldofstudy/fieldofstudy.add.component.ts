import {Component, EventEmitter, Input, OnDestroy, Output} from "@angular/core";
import {FieldOfStudy} from "./fieldofstudy";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {FieldOfStudyDataService} from "./fieldofstudy.data.service";
import {Subject} from "../subject/subject";

@Component({
  templateUrl: 'fieldofstudy.add.component.html',
  selector: 'fieldsOfStudies-add',
})
export class FieldOfStudyAddComponent implements OnDestroy {

  @Input() header = "Przedmioty tego kierunku studiów...";
  fieldOfStudy : FieldOfStudy;
  fieldOfStudySubjects: Subject[] = [];

  private params_subscription: any;

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<FieldOfStudy>();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private fieldOfStudyService: FieldOfStudyDataService) {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for fieldsOfStudies-add ' + id);
      if (id === 'add') {
        this.fieldOfStudy = new FieldOfStudy();

      } else {
        this.fieldOfStudyService.getFieldOfStudy(id)
          .subscribe(fieldOfStudy => {
              this.fieldOfStudy = fieldOfStudy;
              this.fieldOfStudySubjects = fieldOfStudy.subjects;
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
    this.fieldOfStudyService.update(this.fieldOfStudy).
    subscribe(
      fieldOfStudy => {
        this.fieldOfStudy = fieldOfStudy;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.fieldOfStudy);
        } else {
          this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})

        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/subjects', id]);
  }
}