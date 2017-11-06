import {Component} from "@angular/core";
import {UserFieldOfStudyDetailComponent} from "../../component.user/fieldofstudy/fieldofstudy.detail.component";

@Component({
  selector: 'reviewerFieldsOfStudies-add',
  templateUrl: '../../component.user/fieldofstudy/fieldofstudy.detail.component.html',
})
export class ReviewerFieldOfStudyDetailComponent extends UserFieldOfStudyDetailComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerSubjects', id]);
  }
}
