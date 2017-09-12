import {Component, Input, OnDestroy} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {Subject} from "../../component.admin/subject/subject";
import {Laboratory} from "../../component.admin/laboratory/laboratory";
import {FieldOfStudy} from "../../component.admin/fieldofstudy/fieldofstudy";
import {SubjectDataService} from "../../component.admin/subject/subject.data.service";

@Component({
  templateUrl: 'subject.detail.component.html',
  selector: 'userSubjects-detail',
})
export class UserSubjectDetailComponent implements OnDestroy {

  @Input() header = "Laboratoria z tego przedmiotu...";
  subject: Subject;
  subjectLaboratories: Laboratory[] = [];

  private params_subscription: any;

  msgs: Message[] = [];

  fieldOfStudy: FieldOfStudy;

  constructor(private route: ActivatedRoute, private router: Router, private subjectDataService: SubjectDataService) {

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('userSubjects-detail ' + id);
        this.subjectDataService.getSubject(id)
          .subscribe(subject => {
              this.subject = subject;
              this.subjectLaboratories = subject.laboratories;
              this.fieldOfStudy = subject.fieldOfStudy;
            },
            error => this.msgs.push({severity: 'error', summary: 'Constructor error', detail: error})
          );

    });
  }

  ngOnDestroy() {
      this.params_subscription.unsubscribe();
  }

  onRowSelect(event: any) {
    let id = event.data.id;
    this.router.navigate(['/userLaboratories', id]);
  }
}
