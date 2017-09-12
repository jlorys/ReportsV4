import {Component, Input, OnDestroy} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {FieldOfStudy} from "../../component.admin/fieldofstudy/fieldofstudy";
import {Subject} from "../../component.admin/subject/subject";
import {FieldOfStudyDataService} from "../../component.admin/fieldofstudy/fieldofstudy.data.service";

@Component({
  selector: 'userFieldsOfStudies-detail',
  templateUrl: 'fieldofstudy.detail.component.html',
})
export class UserFieldOfStudyDetailComponent implements OnDestroy {

  @Input() header = "Przedmioty tego kierunku studiów...";
  fieldOfStudy : FieldOfStudy;
  fieldOfStudySubjects: Subject[] = [];

  private params_subscription: any;
  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private fieldOfStudyService: FieldOfStudyDataService) {

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for userFieldsOfStudies-detail ' + id);
        this.fieldOfStudyService.getFieldOfStudy(id)
          .subscribe(fieldOfStudy => {
              this.fieldOfStudy = fieldOfStudy;
              this.fieldOfStudySubjects = fieldOfStudy.subjects;
            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
    });
  }

  ngOnDestroy() {
      this.params_subscription.unsubscribe();
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userSubjects', id]);
  }
}
