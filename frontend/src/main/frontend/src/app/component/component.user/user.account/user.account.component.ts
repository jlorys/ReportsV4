import {Component, EventEmitter, Input, Output} from "@angular/core";
import {Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {AppUser} from "../../component.admin/users/user";
import {Report} from "../../component.admin/report/report";
import {UserAccountDataService} from "./user.data.service";

@Component({
  selector: 'usersAccount',
  templateUrl: 'user.account.component.html',
})
export class UserAccountComponent {

  @Input() header = "Sprawozdania użytkownika...";
  user: AppUser;
  userReports: Report[] = [];

  @Output() onSaveClicked = new EventEmitter<AppUser>();

  msgs: Message[] = [];

  constructor(private router: Router, private userAccountDataService: UserAccountDataService) {

    console.log('Constructor for users-account ');

    this.userAccountDataService.getLoggedUser()
      .subscribe(user => {
          this.user = user;
          this.userReports = user.reports;

        },
        error => this.msgs.push({severity: 'error', summary: 'Constructor error', detail: error})
      );
  }

  onRowSelect(event: any) {
    let id = event.data.id;
    this.router.navigate(['/userReports', id]);
  }

  changePassword: boolean = false;

  wantChangePassword() {
    this.changePassword = true;
  }

  oldPassword: string;
  newPassword: string;
  newPasswordRepeat: string;

  onChangePassword() {
    this.msgs = []; //this line fix disappearing of messages

    this.userAccountDataService.changePassword(this.oldPassword, this.newPassword, this.newPasswordRepeat).subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity: 'info', summary: 'Zmieniono hasło', detail: 'OK!'})
      },
      error => this.msgs.push({severity: 'error', summary: 'Dane niepoprawne!', detail: ''})
    );
  }
}
